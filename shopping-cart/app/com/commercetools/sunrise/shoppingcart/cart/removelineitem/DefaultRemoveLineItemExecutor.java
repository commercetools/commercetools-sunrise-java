package com.commercetools.sunrise.shoppingcart.cart.removelineitem;

import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.shoppingcart.AbstractCartUpdateExecutor;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultRemoveLineItemExecutor extends AbstractCartUpdateExecutor implements RemoveLineItemExecutor {

    @Inject
    protected DefaultRemoveLineItemExecutor(final SphereClient sphereClient, final HookContext hookContext) {
        super(sphereClient, hookContext);
    }

    @Override
    public CompletionStage<Cart> apply(final Cart cart, final RemoveLineItemFormData formData) {
        return executeRequest(cart, buildRequest(cart, formData));
    }

    protected CartUpdateCommand buildRequest(final Cart cart, final RemoveLineItemFormData formData) {
        final RemoveLineItem removeLineItem = RemoveLineItem.of(formData.getLineItemId());
        return CartUpdateCommand.of(cart, removeLineItem);
    }
}
