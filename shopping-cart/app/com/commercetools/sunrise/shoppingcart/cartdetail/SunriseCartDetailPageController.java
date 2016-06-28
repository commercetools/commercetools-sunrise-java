package com.commercetools.sunrise.shoppingcart.cartdetail;

import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.errors.ErrorsBean;
import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.ChangeLineItemQuantity;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;
import io.sphere.sdk.client.ErrorResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.libs.concurrent.HttpExecution.defaultContext;

/**
 * Shows and modifies the contents of the cart.
 */
@Singleton
public abstract class SunriseCartDetailPageController extends SunriseFrameworkCartController implements WithOverwriteableTemplateName {
    private static final Logger logger = LoggerFactory.getLogger(SunriseCartDetailPageController.class);

    @Inject
    private FormFactory formFactory;
    @Inject
    private ReverseRouter reverseRouter;
    @Inject
    private CartLikeBeanFactory cartLikeBeanFactory;
    @Inject
    private CartDetailPageContentFactory pageContentFactory;

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> {
            return getOrCreateCart()
                    .thenComposeAsync(cart -> asyncOk(render(pageContentFactory.create(cart))), defaultContext());
        });
    }

    @RequireCSRFCheck
    public CompletionStage<Result> changeLineItemQuantity(final String languageTag) {
        return doRequest(() -> {
        final Form<ChangeLineItemQuantityFormData> changeLineItemQuantityForm = formFactory.form(ChangeLineItemQuantityFormData.class).bindFromRequest();
        return getOrCreateCart()
                .thenComposeAsync(cart -> {
                    if (changeLineItemQuantityForm.hasErrors()) {
                        return handleChangeLineItemQuantityFormErrors(changeLineItemQuantityForm, cart);
                    } else {
                        final String lineItemId = changeLineItemQuantityForm.get().getLineItemId();
                        final Long quantity = changeLineItemQuantityForm.get().getQuantity();
                        final CompletionStage<Result> resultStage = changeLineItemQuantity(lineItemId, quantity, cart)
                                .thenComposeAsync(updatedCart -> handleSuccessfulCartChange(updatedCart), defaultContext());
                        return recoverWithAsync(resultStage, defaultContext(), throwable ->
                                handleChangeLineItemQuantityError(throwable, changeLineItemQuantityForm, cart));
                    }
                }, defaultContext());
        });
    }

    @RequireCSRFCheck
    public CompletionStage<Result> removeLineItem(final String languageTag) {
        return doRequest(() -> {
        final Form<RemoveLineItemFormData> removeLineItemForm = formFactory.form(RemoveLineItemFormData.class).bindFromRequest();
        return getOrCreateCart()
                .thenComposeAsync(cart -> {
                    if (removeLineItemForm.hasErrors()) {
                        return handleRemoveLineItemFormErrors(removeLineItemForm, cart);
                    } else {
                        final String lineItemId = removeLineItemForm.get().getLineItemId();
                        final CompletionStage<Result> resultStage = removeLineItem(lineItemId, cart)
                                .thenComposeAsync(updatedCart -> handleSuccessfulCartChange(updatedCart), defaultContext());
                        return recoverWithAsync(resultStage, defaultContext(), throwable ->
                                handleRemoveLineItemError(throwable, removeLineItemForm, cart));
                    }
                }, defaultContext());
        });
    }

    protected CompletionStage<Cart> changeLineItemQuantity(final String lineItemId, final long quantity, final Cart cart) {
        final ChangeLineItemQuantity changeLineItemQuantity = ChangeLineItemQuantity.of(lineItemId, quantity);
        return sphere().execute(CartUpdateCommand.of(cart, changeLineItemQuantity));
    }

    protected CompletionStage<Cart> removeLineItem(final String lineItemId, final Cart cart) {
        final RemoveLineItem removeLineItem = RemoveLineItem.of(lineItemId);
        return sphere().execute(CartUpdateCommand.of(cart, removeLineItem));
    }

    //TODO this is duplicated
    protected CompletionStage<Result> handleSuccessfulCartChange(final Cart cart) {
        overrideCartSessionData(cart);
        return completedFuture(redirect(reverseRouter.showCart(userContext().languageTag())));
    }

    protected CompletionStage<Result> handleChangeLineItemQuantityFormErrors(final Form<ChangeLineItemQuantityFormData> changeLineItemQuantityForm,
                                                                             final Cart cart) {
        final ErrorsBean errorsBean = new ErrorsBean(changeLineItemQuantityForm);
        final CartDetailPageContent pageContent = createPageContentWithChangeLineItemQuantityError(changeLineItemQuantityForm, errorsBean);
        return asyncBadRequest(renderCartPage(cart, pageContent));
    }

    protected CompletionStage<Result> handleRemoveLineItemFormErrors(final Form<RemoveLineItemFormData> removeLineItemForm,
                                                                     final Cart cart) {
        final ErrorsBean errorsBean = new ErrorsBean(removeLineItemForm);
        final CartDetailPageContent pageContent = createPageContentWithRemoveLineItemError(removeLineItemForm, errorsBean);
        return asyncBadRequest(renderCartPage(cart, pageContent));
    }

    protected CompletionStage<Result> handleChangeLineItemQuantityError(final Throwable throwable,
                                                                        final Form<ChangeLineItemQuantityFormData> changeLineItemQuantityForm,
                                                                        final Cart cart) {
        if (throwable.getCause() instanceof ErrorResponseException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            logger.error("The request to change line item quantity raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            final CartDetailPageContent pageContent = createPageContentWithChangeLineItemQuantityError(changeLineItemQuantityForm, errors);
            return asyncBadRequest(renderCartPage(cart, pageContent));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CompletionStage<Result> handleRemoveLineItemError(final Throwable throwable,
                                                                final Form<RemoveLineItemFormData> removeLineItemForm,
                                                                final Cart cart) {
        if (throwable.getCause() instanceof ErrorResponseException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            logger.error("The request to remove line item raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            final CartDetailPageContent pageContent = createPageContentWithRemoveLineItemError(removeLineItemForm, errors);
            return asyncBadRequest(renderCartPage(cart, pageContent));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CartDetailPageContent createPageContentWithChangeLineItemQuantityError(final Form<ChangeLineItemQuantityFormData> changeLineItemQuantityForm,
                                                                                     final ErrorsBean errors) {
        // TODO placeholder to put cart form errors
        return new CartDetailPageContent();
    }

    protected CartDetailPageContent createPageContentWithRemoveLineItemError(final Form<RemoveLineItemFormData> removeLineItemForm,
                                                                             final ErrorsBean errors) {
        // TODO placeholder to put cart form errors
        return new CartDetailPageContent();
    }

    protected CompletionStage<Html> renderCartPage(final Cart cart, final CartDetailPageContent pageContent) {
        pageContent.setCart(cartLikeBeanFactory.create(cart));
        setI18nTitle(pageContent, "checkout:cartDetailPage.title");
        return render(pageContent);
    }

    private CompletionStage<Html> render(final CartDetailPageContent pageContent) {
        return renderPage(pageContent, getTemplateName());
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("cart", "cart-detail"));
    }
}
