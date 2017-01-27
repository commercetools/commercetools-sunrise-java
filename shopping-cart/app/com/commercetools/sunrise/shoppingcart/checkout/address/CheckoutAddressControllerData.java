package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.shoppingcart.common.EditableCartControllerData;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.annotation.Nullable;

public class CheckoutAddressControllerData extends EditableCartControllerData<CheckoutAddressFormData> {

    public CheckoutAddressControllerData(final Form<? extends CheckoutAddressFormData> form, final Cart cart, @Nullable final Cart updatedCart) {
        super(form, cart, updatedCart);
    }

    @Override
    public Form<? extends CheckoutAddressFormData> getForm() {
        return super.getForm();
    }

    @Override
    public Cart getCart() {
        return super.getCart();
    }
}
