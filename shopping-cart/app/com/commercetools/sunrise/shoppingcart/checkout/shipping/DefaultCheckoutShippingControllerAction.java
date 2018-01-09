package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.models.carts.MyCartUpdater;
import io.sphere.sdk.carts.Cart;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultCheckoutShippingControllerAction implements CheckoutShippingControllerAction {

    private final MyCartUpdater myCartUpdater;

    @Inject
    DefaultCheckoutShippingControllerAction(final MyCartUpdater myCartUpdater) {
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    public CompletionStage<Cart> apply(final CheckoutShippingFormData formData) {
        return myCartUpdater.force(formData.updateActions());
    }
}
