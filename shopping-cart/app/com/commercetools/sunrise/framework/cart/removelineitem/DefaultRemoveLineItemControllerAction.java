package com.commercetools.sunrise.framework.cart.removelineitem;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.AbstractCartUpdateExecutor;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultRemoveLineItemControllerAction extends AbstractCartUpdateExecutor implements RemoveLineItemControllerAction {

    @Inject
    protected DefaultRemoveLineItemControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Cart> apply(final Cart cart, final RemoveLineItemFormData formData) {
        return executeRequest(cart, buildRequest(cart, formData));
    }

    protected CartUpdateCommand buildRequest(final Cart cart, final RemoveLineItemFormData formData) {
        final RemoveLineItem removeLineItem = RemoveLineItem.of(formData.lineItemId());
        return CartUpdateCommand.of(cart, removeLineItem);
    }
}
