package com.commercetools.sunrise.shoppingcart.add;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.shoppingcart.AbstractCartUpdateExecutor;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultAddToCartControllerAction extends AbstractCartUpdateExecutor implements AddToCartControllerAction {

    @Inject
    protected DefaultAddToCartControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Cart> apply(final Cart cart, final AddToCartFormData formData) {
        return executeRequest(cart, buildRequest(cart, formData));
    }

    protected CartUpdateCommand buildRequest(final Cart cart, final AddToCartFormData formData) {
        final AddLineItem updateAction = AddLineItem.of(formData.productId(), formData.variantId(), formData.quantity());
        return CartUpdateCommand.of(cart, updateAction);
    }
}
