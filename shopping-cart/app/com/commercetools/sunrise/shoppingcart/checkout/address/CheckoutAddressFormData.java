package com.commercetools.sunrise.shoppingcart.checkout.address;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;

public interface CheckoutAddressFormData {
    void setData(final Cart cart);

    Address toShippingAddress();

    @Nullable Address toBillingAddress();
}
