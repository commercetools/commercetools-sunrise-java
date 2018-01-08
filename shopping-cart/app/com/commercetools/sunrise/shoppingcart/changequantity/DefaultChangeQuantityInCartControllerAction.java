package com.commercetools.sunrise.shoppingcart.changequantity;

import com.commercetools.sunrise.models.carts.MyCartUpdater;
import io.sphere.sdk.carts.Cart;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultChangeQuantityInCartControllerAction implements ChangeQuantityInCartControllerAction {

    private final MyCartUpdater myCartUpdater;

    @Inject
    DefaultChangeQuantityInCartControllerAction(final MyCartUpdater myCartUpdater) {
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    public CompletionStage<Cart> apply(final ChangeQuantityInCartFormData formData) {
        return myCartUpdater.force(formData.changeQuantity());
    }
}
