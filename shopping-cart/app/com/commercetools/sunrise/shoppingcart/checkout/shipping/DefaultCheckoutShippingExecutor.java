package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.shoppingcart.AbstractCartUpdateExecutor;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetShippingMethod;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultCheckoutShippingExecutor extends AbstractCartUpdateExecutor implements CheckoutShippingExecutor {

    @Inject
    protected DefaultCheckoutShippingExecutor(final SphereClient sphereClient, final HookContext hookContext) {
        super(sphereClient, hookContext);
    }

    @Override
    public CompletionStage<Cart> apply(final Cart cart, final CheckoutShippingFormData formData) {
        return executeRequest(cart, buildRequest(cart, formData));
    }

    protected CartUpdateCommand buildRequest(final Cart cart, final CheckoutShippingFormData formData) {
        final Reference<ShippingMethod> shippingMethodRef = ShippingMethod.referenceOfId(formData.getShippingMethodId());
        final SetShippingMethod setShippingMethod = SetShippingMethod.of(shippingMethodRef);
        return CartUpdateCommand.of(cart, setShippingMethod);
    }
}
