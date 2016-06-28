package com.commercetools.sunrise.shoppingcart.changelineitemquantity;

import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.errors.ErrorsBean;
import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import com.commercetools.sunrise.shoppingcart.cartdetail.CartDetailPageContent;
import com.commercetools.sunrise.shoppingcart.cartdetail.ChangeLineItemQuantityFormData;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.ChangeLineItemQuantity;
import io.sphere.sdk.client.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.inject.Inject;
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
public abstract class SunriseChangeLineItemQuantityController extends SunriseFrameworkCartController implements WithOverwriteableTemplateName {
    private static final Logger logger = LoggerFactory.getLogger(SunriseChangeLineItemQuantityController.class);

    @Inject
    private ReverseRouter reverseRouter;
    @Inject
    private CartLikeBeanFactory cartLikeBeanFactory;

    @RequireCSRFCheck
    public CompletionStage<Result> changeLineItemQuantity(final String languageTag) {
        return doRequest(() -> {
            final Form<ChangeLineItemQuantityFormData> filledForm = formFactory().form(ChangeLineItemQuantityFormData.class).bindFromRequest();
            return filledForm.hasErrors() ? handleInvalidForm(filledForm) : handleValidForm(filledForm);
        });
    }

    private CompletionStage<Result> handleValidForm(final Form<ChangeLineItemQuantityFormData> form) {
        return getOrCreateCart()
                .thenComposeAsync(cart -> {
                    final String lineItemId = form.get().getLineItemId();
                    final Long quantity = form.get().getQuantity();
                    final CompletionStage<Result> resultStage = changeLineItemQuantity(lineItemId, quantity, cart)
                            .thenComposeAsync(updatedCart -> handleSuccessfulCartChange(updatedCart), defaultContext());
                    return recoverWithAsync(resultStage, defaultContext(), throwable ->
                            handleChangeLineItemQuantityError(throwable, form, cart));
                }, defaultContext());
    }

    protected CompletionStage<Result> handleInvalidForm(final Form<ChangeLineItemQuantityFormData> form) {
        return getOrCreateCart()
                .thenComposeAsync(cart -> {
                    final ErrorsBean errorsBean = new ErrorsBean(form);
                    final CartDetailPageContent pageContent = createPageContentWithChangeLineItemQuantityError(form, errorsBean);
                    return asyncBadRequest(renderCartPage(cart, pageContent));
                }, defaultContext());
    }

    protected CompletionStage<Cart> changeLineItemQuantity(final String lineItemId, final long quantity, final Cart cart) {
        final ChangeLineItemQuantity changeLineItemQuantity = ChangeLineItemQuantity.of(lineItemId, quantity);
        return sphere().execute(CartUpdateCommand.of(cart, changeLineItemQuantity));
    }

    //TODO this is duplicated
    protected CompletionStage<Result> handleSuccessfulCartChange(final Cart cart) {
        overrideCartSessionData(cart);
        return completedFuture(redirect(reverseRouter.showCart(userContext().languageTag())));
    }

    protected CompletionStage<Result> handleChangeLineItemQuantityError(final Throwable throwable,
                                                                        final Form<ChangeLineItemQuantityFormData> changeLineItemQuantityForm,
                                                                        final Cart cart) {
        if (throwable.getCause() instanceof BadRequestException) {
            logger.error("The request to change line item quantity raised an exception", throwable);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            final CartDetailPageContent pageContent = createPageContentWithChangeLineItemQuantityError(changeLineItemQuantityForm, errors);
            return asyncBadRequest(renderCartPage(cart, pageContent));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CartDetailPageContent createPageContentWithChangeLineItemQuantityError(final Form<ChangeLineItemQuantityFormData> changeLineItemQuantityForm,
                                                                                     final ErrorsBean errors) {
        // TODO placeholder to put cart form errors
        return new CartDetailPageContent();
    }

    //TODO duplicated
    protected CompletionStage<Html> renderCartPage(final Cart cart, final CartDetailPageContent pageContent) {
        pageContent.setCart(cartLikeBeanFactory.create(cart));
        setI18nTitle(pageContent, "checkout:cartDetailPage.title");
        return renderPage(pageContent, getTemplateName());
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("cart"));
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }
}
