package com.commercetools.sunrise.shoppingcart.changequantity;

import com.commercetools.sunrise.models.carts.MyCartUpdater;
import io.sphere.sdk.carts.Cart;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultChangeQuantityInCartFormAction implements ChangeQuantityInCartFormAction {

    private final MyCartUpdater myCartUpdater;

    @Inject
    DefaultChangeQuantityInCartFormAction(final MyCartUpdater myCartUpdater) {
        this.myCartUpdater = myCartUpdater;
    }

    @Override
    public CompletionStage<Cart> apply(final ChangeQuantityInCartFormData formData) {
        return myCartUpdater.force(formData.changeQuantity());
    }
}
