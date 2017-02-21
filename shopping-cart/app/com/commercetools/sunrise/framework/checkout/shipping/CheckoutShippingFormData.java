package com.commercetools.sunrise.framework.checkout.shipping;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultCheckoutShippingFormData.class)
public interface CheckoutShippingFormData {

    String obtainShippingMethod();

    void applyShippingMethod(final String shippingMethod);
}
