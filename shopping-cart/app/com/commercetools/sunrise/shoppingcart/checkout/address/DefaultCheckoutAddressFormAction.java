package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.models.carts.MyCartUpdater;
import io.sphere.sdk.carts.Cart;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultCheckoutAddressFormAction implements CheckoutAddressFormAction {

    private MyCartUpdater myCartUpdater;

    @Inject
    DefaultCheckoutAddressFormAction(final MyCartUpdater myCartUpdater) {
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    public CompletionStage<Cart> apply(final CheckoutAddressFormData formData) {
        return myCartUpdater.force(formData.updateActions());
    }
}
