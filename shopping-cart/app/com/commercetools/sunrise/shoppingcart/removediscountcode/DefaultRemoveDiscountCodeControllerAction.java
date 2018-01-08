package com.commercetools.sunrise.shoppingcart.removediscountcode;

import com.commercetools.sunrise.models.carts.MyCartUpdater;
import com.google.inject.Inject;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

final class DefaultRemoveDiscountCodeControllerAction implements RemoveDiscountCodeControllerAction {

    private final MyCartUpdater myCartUpdater;

    @Inject
    DefaultRemoveDiscountCodeControllerAction(final MyCartUpdater myCartUpdater) {
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    public CompletionStage<Cart> apply(final RemoveDiscountCodeFormData formData) {
        return myCartUpdater.force(formData.removeDiscountCode());
    }
}
