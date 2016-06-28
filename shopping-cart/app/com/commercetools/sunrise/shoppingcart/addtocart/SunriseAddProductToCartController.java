package com.commercetools.sunrise.shoppingcart.addtocart;

import com.commercetools.sunrise.common.controllers.FormBindingTrait;
import com.commercetools.sunrise.common.forms.UserFeedback;
import com.commercetools.sunrise.shoppingcart.cartdetail.AddProductToCartFormData;
import com.commercetools.sunrise.shoppingcart.cartdetail.AddProductToCartFormDataLike;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import com.google.inject.Injector;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static play.libs.concurrent.HttpExecution.defaultContext;

public abstract class SunriseAddProductToCartController extends SunriseFrameworkCartController implements FormBindingTrait<AddProductToCartFormDataLike> {
    private static final Logger logger = LoggerFactory.getLogger(SunriseAddProductToCartController.class);
    @Inject
    private Injector injector;

    @RequireCSRFCheck
    public CompletionStage<Result> addProductToCart(final String languageTag) {
        return doRequest(() -> bindForm()
                .thenComposeAsync(filledForm ->
                        filledForm.hasErrors() ? handleInvalidForm(filledForm) : handleValidForm(filledForm), defaultContext()
                ));
    }

    private CompletionStage<Result> handleValidForm(final Form<? extends AddProductToCartFormDataLike> filledForm) {
        return getOrCreateCart()
                .thenComposeAsync(cart -> {
                    final AddProductToCartFormDataLike data = filledForm.get();
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
    public Class<? extends AddProductToCartFormDataLike> getFormDataClass() {
        return AddProductToCartFormData.class;
    }

    protected CompletionStage<Result> handleInvalidForm(final Form<? extends AddProductToCartFormDataLike> form) {
        injector.getInstance(UserFeedback.class).addErrors(form);
        return successfulResult();
    }

    protected CompletionStage<Result> handleSuccessfulCartChange(final Cart cart) {
        overrideCartSessionData(cart);
        return successfulResult();
    }

    protected abstract CompletableFuture<Result> successfulResult();

    protected CompletionStage<Result> handleAddProductToCartError(final Throwable throwable,
                                                                  final Form<? extends AddProductToCartFormDataLike> form,
                                                                  final Cart cart) {
        flash(UserFeedback.ERROR, "an error occurred");// TODO get from i18n
        return successfulResult();
    }

    protected CompletionStage<Cart> addProductToCart(final String productId, final Integer variantId, final Long quantity, final Cart cart) {
        final AddLineItem updateAction = AddLineItem.of(productId, variantId, quantity);
        return sphere().execute(CartUpdateCommand.of(cart, updateAction));//TODO introduce filter hook hand update session cart, log changed cart
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("cart", "add-line-item-to-cart"));
    }
}
