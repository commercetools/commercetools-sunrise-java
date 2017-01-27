package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.shoppingcart.checkout.CheckoutWithUpdatedCartPageData;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.annotation.Nullable;

public class CheckoutAddressPageData extends CheckoutWithUpdatedCartPageData {

    public CheckoutAddressPageData(final Form<?> form, final Cart cart, @Nullable final Cart updatedCart) {
        super(form, cart, updatedCart);
    }
}
