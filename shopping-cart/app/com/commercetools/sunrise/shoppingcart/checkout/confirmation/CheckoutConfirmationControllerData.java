package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.shoppingcart.common.EditableCartControllerData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.orders.Order;
import play.data.Form;

import javax.annotation.Nullable;

public class CheckoutConfirmationControllerData extends EditableCartControllerData<CheckoutConfirmationFormData> {

    public CheckoutConfirmationControllerData(final Form<? extends CheckoutConfirmationFormData> form, final Cart cart, @Nullable final Order order) {
        super(form, cart, null);
    }

    @Override
    public Cart getCart() {
        return super.getCart();
    }

    @Override
    public Form<? extends CheckoutConfirmationFormData> getForm() {
        return super.getForm();
    }
}
