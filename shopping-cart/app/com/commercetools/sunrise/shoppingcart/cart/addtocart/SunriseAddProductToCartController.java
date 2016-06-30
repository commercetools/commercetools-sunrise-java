package com.commercetools.sunrise.shoppingcart.cart.addtocart;

import com.commercetools.sunrise.common.controllers.FormBindingTrait;
import com.commercetools.sunrise.common.forms.UserFeedback;
import com.commercetools.sunrise.hooks.CartUpdateCommandFilterHook;
import com.commercetools.sunrise.hooks.UserCartUpdatedHook;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static play.libs.concurrent.HttpExecution.defaultContext;

public abstract class SunriseAddProductToCartController extends SunriseFrameworkCartController implements FormBindingTrait<AddProductToCartFormData> {
    private static final Logger logger = LoggerFactory.getLogger(SunriseAddProductToCartController.class);

    @RequireCSRFCheck
    public CompletionStage<Result> addProductToCart(final String languageTag) {
        return doRequest(() -> bindForm()
                .thenComposeAsync(filledForm ->
                        filledForm.hasErrors() ? handleInvalidForm(filledForm) : handleValidForm(filledForm), defaultContext()
                ));
    }

    private CompletionStage<Result> handleValidForm(final Form<? extends AddProductToCartFormData> filledForm) {
        return getOrCreateCart()
                .thenComposeAsync(cart -> {
                    final AddProductToCartFormData data = filledForm.get();
                    final String productId = data.getProductId();
                    final int variantId = data.getVariantId();
                    final long quantity = data.getQuantity();
                    final CompletionStage<Result> resultStage = addProductToCart(productId, variantId, quantity, cart)
                            .thenComposeAsync(updatedCart -> handleSuccessfulCartChange(updatedCart), defaultContext());
                    return recoverWithAsync(resultStage, defaultContext(), throwable ->
                            handleAddProductToCartError(throwable, filledForm, cart));
                }, defaultContext());
    }

    @Override
    public Class<? extends AddProductToCartFormData> getFormDataClass() {
        return DefaultAddProductToCartFormData.class;
    }

    protected CompletionStage<Result> handleInvalidForm(final Form<? extends AddProductToCartFormData> form) {
        injector().getInstance(UserFeedback.class).addErrors(form);
        return successfulResult();
    }

    protected CompletionStage<Result> handleSuccessfulCartChange(final Cart cart) {
        overrideCartSessionData(cart);
        return successfulResult();
    }

    protected abstract CompletableFuture<Result> successfulResult();

    protected CompletionStage<Result> handleAddProductToCartError(final Throwable throwable,
                                                                  final Form<? extends AddProductToCartFormData> form,
                                                                  final Cart cart) {
        injector().getInstance(UserFeedback.class).addErrors("an error occurred");// TODO get from i18n
        return successfulResult();
    }

    protected CompletionStage<Cart> addProductToCart(final String productId, final Integer variantId, final Long quantity, final Cart cart) {
        final AddLineItem updateAction = AddLineItem.of(productId, variantId, quantity);
        return executeSphereRequestWithHooks(
                CartUpdateCommand.of(cart, updateAction),
                CartUpdateCommandFilterHook.class, CartUpdateCommandFilterHook::filterCartUpdateCommand,
                UserCartUpdatedHook.class, UserCartUpdatedHook::onUserCartUpdated
        );
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("cart", "add-line-item-to-cart"));
    }
}
