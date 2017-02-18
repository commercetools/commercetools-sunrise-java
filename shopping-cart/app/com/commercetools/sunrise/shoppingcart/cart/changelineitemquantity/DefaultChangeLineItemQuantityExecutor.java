package com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.shoppingcart.AbstractCartUpdateExecutor;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.ChangeLineItemQuantity;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultChangeLineItemQuantityExecutor extends AbstractCartUpdateExecutor implements ChangeLineItemQuantityExecutor {

    @Inject
    protected DefaultChangeLineItemQuantityExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Cart> apply(final Cart cart, final ChangeLineItemQuantityFormData formData) {
        return executeRequest(cart, buildRequest(cart, formData));
    }

    protected CartUpdateCommand buildRequest(final Cart cart, final ChangeLineItemQuantityFormData formData) {
        final ChangeLineItemQuantity updateAction = ChangeLineItemQuantity.of(formData.getLineItemId(), formData.getQuantity());
        return CartUpdateCommand.of(cart, updateAction);
    }
}
