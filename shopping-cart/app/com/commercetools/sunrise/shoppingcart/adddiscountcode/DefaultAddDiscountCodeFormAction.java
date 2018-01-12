package com.commercetools.sunrise.shoppingcart.adddiscountcode;

import com.commercetools.sunrise.models.carts.MyCartUpdater;
import com.google.inject.Inject;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

final class DefaultAddDiscountCodeFormAction implements AddDiscountCodeFormAction {

    private final MyCartUpdater myCartUpdater;

    @Inject
    DefaultAddDiscountCodeFormAction(final MyCartUpdater myCartUpdater) {
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    public CompletionStage<Cart> apply(final AddDiscountCodeFormData formData) {
        return myCartUpdater.force(formData.addDiscountCode());
    }
}
