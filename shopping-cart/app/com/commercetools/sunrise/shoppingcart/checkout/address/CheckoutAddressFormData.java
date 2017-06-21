package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;

@ImplementedBy(DefaultCheckoutAddressFormData.class)
public interface CheckoutAddressFormData {

    Address shippingAddress();

    @Nullable Address billingAddress();

    void applyCart(final Cart cart);
}
