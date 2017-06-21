package com.commercetools.sunrise.shoppingcart.remove;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.shoppingcart.AbstractCartUpdateExecutor;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultRemoveFromCartControllerAction extends AbstractCartUpdateExecutor implements RemoveFromCartControllerAction {

    @Inject
    protected DefaultRemoveFromCartControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Cart> apply(final Cart cart, final RemoveFromCartFormData formData) {
        return executeRequest(cart, buildRequest(cart, formData));
    }

    protected CartUpdateCommand buildRequest(final Cart cart, final RemoveFromCartFormData formData) {
        final RemoveLineItem removeLineItem = RemoveLineItem.of(formData.lineItemId());
        return CartUpdateCommand.of(cart, removeLineItem);
    }
}
