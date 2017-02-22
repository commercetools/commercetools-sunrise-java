package com.commercetools.sunrise.framework.checkout.shipping;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.AbstractCartUpdateExecutor;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetShippingMethod;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultCheckoutShippingControllerAction extends AbstractCartUpdateExecutor implements CheckoutShippingControllerAction {

    @Inject
    protected DefaultCheckoutShippingControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Cart> apply(final ShippingMethodsWithCart shippingMethodsWithCart, final CheckoutShippingFormData formData) {
        return executeRequest(shippingMethodsWithCart.getCart(), buildRequest(shippingMethodsWithCart, formData));
    }

    protected CartUpdateCommand buildRequest(final ShippingMethodsWithCart shippingMethodsWithCart, final CheckoutShippingFormData formData) {
        final Reference<ShippingMethod> shippingMethodRef = ShippingMethod.referenceOfId(formData.shippingMethod());
        final SetShippingMethod setShippingMethod = SetShippingMethod.of(shippingMethodRef);
        return CartUpdateCommand.of(shippingMethodsWithCart.getCart(), setShippingMethod);
    }
}
