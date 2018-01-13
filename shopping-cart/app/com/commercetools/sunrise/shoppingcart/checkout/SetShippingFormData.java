package com.commercetools.sunrise.shoppingcart.checkout;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.SetShippingMethod;

@ImplementedBy(DefaultSetShippingFormData.class)
public interface SetShippingFormData {

    SetShippingMethod setShippingMethod();
}
