package com.commercetools.sunrise.shoppingcart.adddiscountcode;

import com.commercetools.sunrise.models.carts.MyCartUpdater;
import com.google.inject.Inject;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

final class DefaultAddDiscountCodeControllerAction implements AddDiscountCodeControllerAction {

    private final MyCartUpdater myCartUpdater;

    @Inject
    DefaultAddDiscountCodeControllerAction(final MyCartUpdater myCartUpdater) {
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    public CompletionStage<Cart> apply(final AddDiscountCodeFormData formData) {
        return myCartUpdater.force(formData.addDiscountCode());
    }
}
