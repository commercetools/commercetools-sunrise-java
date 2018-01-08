package com.commercetools.sunrise.shoppingcart.remove;

import com.commercetools.sunrise.models.carts.MyCartUpdater;
import io.sphere.sdk.carts.Cart;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultRemoveFromCartControllerAction implements RemoveFromCartControllerAction {

    private final MyCartUpdater myCartUpdater;

    @Inject
    DefaultRemoveFromCartControllerAction(final MyCartUpdater myCartUpdater) {
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    public CompletionStage<Cart> apply(final RemoveFromCartFormData formData) {
        return myCartUpdater.force(formData.removeLineItem());
    }
}
