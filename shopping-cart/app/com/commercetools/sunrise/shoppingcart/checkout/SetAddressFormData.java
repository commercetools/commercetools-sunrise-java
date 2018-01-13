package com.commercetools.sunrise.shoppingcart.checkout;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.SetBillingAddress;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;

@ImplementedBy(DefaultSetAddressFormData.class)
public interface SetAddressFormData {

    SetShippingAddress setShippingAddress();

    SetBillingAddress setBillingAddress();
}
