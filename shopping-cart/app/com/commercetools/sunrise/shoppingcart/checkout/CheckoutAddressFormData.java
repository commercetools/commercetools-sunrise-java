package com.commercetools.sunrise.shoppingcart.checkout;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.SetBillingAddress;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;

@ImplementedBy(DefaultCheckoutAddressFormData.class)
public interface CheckoutAddressFormData {

    SetShippingAddress setShippingAddress();

    SetBillingAddress setBillingAddress();
}
