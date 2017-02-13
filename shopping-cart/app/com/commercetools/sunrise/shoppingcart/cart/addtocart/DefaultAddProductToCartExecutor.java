package com.commercetools.sunrise.shoppingcart.cart.addtocart;

import com.commercetools.sunrise.hooks.HookRunner;
import com.commercetools.sunrise.shoppingcart.AbstractCartUpdateExecutor;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultAddProductToCartExecutor extends AbstractCartUpdateExecutor implements AddProductToCartExecutor {

    @Inject
    protected DefaultAddProductToCartExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Cart> apply(final Cart cart, final AddProductToCartFormData formData) {
        return executeRequest(cart, buildRequest(cart, formData));
    }

    protected CartUpdateCommand buildRequest(final Cart cart, final AddProductToCartFormData formData) {
        final AddLineItem updateAction = AddLineItem.of(formData.getProductId(), formData.getVariantId(), formData.getQuantity());
        return CartUpdateCommand.of(cart, updateAction);
    }
}
