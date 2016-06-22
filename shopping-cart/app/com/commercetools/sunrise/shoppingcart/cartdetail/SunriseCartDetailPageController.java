package com.commercetools.sunrise.shoppingcart.cartdetail;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.errors.ErrorsBean;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import io.sphere.sdk.carts.commands.updateactions.ChangeLineItemQuantity;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;
import io.sphere.sdk.client.ErrorResponseException;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.shoppingcart.CartSessionUtils.overwriteCartSessionData;
import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Shows and modifies the contents of the cart.
 */
@Singleton
public abstract class SunriseCartDetailPageController extends SunriseFrameworkCartController implements WithOverwriteableTemplateName {

    @Inject
    private FormFactory formFactory;
    @Inject
    private ProductReverseRouter productReverseRouter;
    @Inject
    private ReverseRouter reverseRouter;

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> {
            final CartDetailPageContent pageContent = createPageContent(userContext());
            return getOrCreateCart(userContext(), session())
                    .thenComposeAsync(cart -> asyncOk(renderCartPage(cart, pageContent, userContext())), HttpExecution.defaultContext());
        });
    }

    public CompletionStage<Result> addProductToCart(final String languageTag) {
        return doRequest(() -> {
        final Form<AddProductToCartFormData> addProductToCartForm = formFactory.form(AddProductToCartFormData.class).bindFromRequest();
        return getOrCreateCart()
                .thenComposeAsync(cart -> {
                    if (addProductToCartForm.hasErrors()) {
                        return handleAddProductToCartFormErrors(addProductToCartForm, cart, userContext());
                    } else {
                        final String productId = addProductToCartForm.get().getProductId();
                        final int variantId = addProductToCartForm.get().getVariantId();
                        final long quantity = addProductToCartForm.get().getQuantity();
                        final CompletionStage<Result> resultStage = addProductToCart(productId, variantId, quantity, cart)
                                .thenComposeAsync(updatedCart -> handleSuccessfulCartChange(updatedCart, userContext()), HttpExecution.defaultContext());
                        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                                handleAddProductToCartError(throwable, addProductToCartForm, cart, userContext()));
                    }
                }, HttpExecution.defaultContext());
        });
    }

    @RequireCSRFCheck
    public CompletionStage<Result> changeLineItemQuantity(final String languageTag) {
        return doRequest(() -> {
        final Form<ChangeLineItemQuantityFormData> changeLineItemQuantityForm = formFactory.form(ChangeLineItemQuantityFormData.class).bindFromRequest();
        return getOrCreateCart()
                .thenComposeAsync(cart -> {
                    if (changeLineItemQuantityForm.hasErrors()) {
                        return handleChangeLineItemQuantityFormErrors(changeLineItemQuantityForm, cart, userContext());
                    } else {
                        final String lineItemId = changeLineItemQuantityForm.get().getLineItemId();
                        final Long quantity = changeLineItemQuantityForm.get().getQuantity();
                        final CompletionStage<Result> resultStage = changeLineItemQuantity(lineItemId, quantity, cart)
                                .thenComposeAsync(updatedCart -> handleSuccessfulCartChange(updatedCart, userContext()), HttpExecution.defaultContext());
                        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                                handleChangeLineItemQuantityError(throwable, changeLineItemQuantityForm, cart, userContext()));
                    }
                }, HttpExecution.defaultContext());
        });
    }

    @RequireCSRFCheck
    public CompletionStage<Result> removeLineItem(final String languageTag) {
        return doRequest(() -> {
        final Form<RemoveLineItemFormData> removeLineItemForm = formFactory.form(RemoveLineItemFormData.class).bindFromRequest();
        return getOrCreateCart()
                .thenComposeAsync(cart -> {
                    if (removeLineItemForm.hasErrors()) {
                        return handleRemoveLineItemFormErrors(removeLineItemForm, cart, userContext());
                    } else {
                        final String lineItemId = removeLineItemForm.get().getLineItemId();
                        final CompletionStage<Result> resultStage = removeLineItem(lineItemId, cart)
                                .thenComposeAsync(updatedCart -> handleSuccessfulCartChange(updatedCart, userContext()), HttpExecution.defaultContext());
                        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                                handleRemoveLineItemError(throwable, removeLineItemForm, cart, userContext()));
                    }
                }, HttpExecution.defaultContext());
        });
    }

    protected CompletionStage<Cart> addProductToCart(final String productId, final int variantId, final long quantity, final Cart cart) {
        final AddLineItem updateAction = AddLineItem.of(productId, variantId, quantity);
        return sphere().execute(CartUpdateCommand.of(cart, updateAction));
    }

    protected CompletionStage<Cart> changeLineItemQuantity(final String lineItemId, final long quantity, final Cart cart) {
        final ChangeLineItemQuantity changeLineItemQuantity = ChangeLineItemQuantity.of(lineItemId, quantity);
        return sphere().execute(CartUpdateCommand.of(cart, changeLineItemQuantity));
    }

    protected CompletionStage<Cart> removeLineItem(final String lineItemId, final Cart cart) {
        final RemoveLineItem removeLineItem = RemoveLineItem.of(lineItemId);
        return sphere().execute(CartUpdateCommand.of(cart, removeLineItem));
    }

    protected CompletionStage<Result> handleSuccessfulCartChange(final Cart cart, final UserContext userContext) {
        overwriteCartSessionData(cart, session(), userContext, productReverseRouter);
        return completedFuture(redirect(reverseRouter.showCart(userContext.locale().toLanguageTag())));
    }

    protected CompletionStage<Result> handleAddProductToCartFormErrors(final Form<AddProductToCartFormData> addProductToCartForm,
                                                                       final Cart cart, final UserContext userContext) {
        final ErrorsBean errorsBean = new ErrorsBean(addProductToCartForm);
        final CartDetailPageContent pageContent = createPageContentWithAddProductToCartError(addProductToCartForm, errorsBean, userContext);
        return asyncBadRequest(renderCartPage(cart, pageContent, userContext));
    }

    protected CompletionStage<Result> handleChangeLineItemQuantityFormErrors(final Form<ChangeLineItemQuantityFormData> changeLineItemQuantityForm,
                                                                             final Cart cart, final UserContext userContext) {
        final ErrorsBean errorsBean = new ErrorsBean(changeLineItemQuantityForm);
        final CartDetailPageContent pageContent = createPageContentWithChangeLineItemQuantityError(changeLineItemQuantityForm, errorsBean, userContext);
        return asyncBadRequest(renderCartPage(cart, pageContent, userContext));
    }

    protected CompletionStage<Result> handleRemoveLineItemFormErrors(final Form<RemoveLineItemFormData> removeLineItemForm,
                                                                     final Cart cart, final UserContext userContext) {
        final ErrorsBean errorsBean = new ErrorsBean(removeLineItemForm);
        final CartDetailPageContent pageContent = createPageContentWithRemoveLineItemError(removeLineItemForm, errorsBean, userContext);
        return asyncBadRequest(renderCartPage(cart, pageContent, userContext));
    }

    protected CompletionStage<Result> handleAddProductToCartError(final Throwable throwable,
                                                                  final Form<AddProductToCartFormData> addProductToCartForm,
                                                                  final Cart cart, final UserContext userContext) {
        if (throwable.getCause() instanceof ErrorResponseException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            Logger.error("The request to add product to cart raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            final CartDetailPageContent pageContent = createPageContentWithAddProductToCartError(addProductToCartForm, errors, userContext);
            return asyncBadRequest(renderCartPage(cart, pageContent, userContext));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CompletionStage<Result> handleChangeLineItemQuantityError(final Throwable throwable,
                                                                        final Form<ChangeLineItemQuantityFormData> changeLineItemQuantityForm,
                                                                        final Cart cart, final UserContext userContext) {
        if (throwable.getCause() instanceof ErrorResponseException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            Logger.error("The request to change line item quantity raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            final CartDetailPageContent pageContent = createPageContentWithChangeLineItemQuantityError(changeLineItemQuantityForm, errors, userContext);
            return asyncBadRequest(renderCartPage(cart, pageContent, userContext));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CompletionStage<Result> handleRemoveLineItemError(final Throwable throwable,
                                                                final Form<RemoveLineItemFormData> removeLineItemForm,
                                                                final Cart cart, final UserContext userContext) {
        if (throwable.getCause() instanceof ErrorResponseException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            Logger.error("The request to remove line item raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            final CartDetailPageContent pageContent = createPageContentWithRemoveLineItemError(removeLineItemForm, errors, userContext);
            return asyncBadRequest(renderCartPage(cart, pageContent, userContext));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CartDetailPageContent createPageContent(final UserContext userContext) {
        return new CartDetailPageContent();
    }

    protected CartDetailPageContent createPageContentWithAddProductToCartError(final Form<AddProductToCartFormData> addProductToCartForm,
                                                                               final ErrorsBean errors, final UserContext userContext) {
        // TODO placeholder to put cart form errors
        return new CartDetailPageContent();
    }

    protected CartDetailPageContent createPageContentWithChangeLineItemQuantityError(final Form<ChangeLineItemQuantityFormData> changeLineItemQuantityForm,
                                                                                     final ErrorsBean errors, final UserContext userContext) {
        // TODO placeholder to put cart form errors
        return new CartDetailPageContent();
    }

    protected CartDetailPageContent createPageContentWithRemoveLineItemError(final Form<RemoveLineItemFormData> removeLineItemForm,
                                                                             final ErrorsBean errors, final UserContext userContext) {
        // TODO placeholder to put cart form errors
        return new CartDetailPageContent();
    }

    protected CompletionStage<Html> renderCartPage(final Cart cart, final CartDetailPageContent pageContent, final UserContext userContext) {
        pageContent.setCart(createCartLikeBean(cart, userContext));
        pageContent.setTitle(i18nResolver().getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:cartDetailPage.title")));
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
