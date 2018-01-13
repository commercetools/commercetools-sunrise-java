package com.commercetools.sunrise.shoppingcart.checkout;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.SetShippingMethod;

@ImplementedBy(DefaultCheckoutShippingFormData.class)
public interface CheckoutShippingFormData {

    SetShippingMethod setShippingMethod();
}
