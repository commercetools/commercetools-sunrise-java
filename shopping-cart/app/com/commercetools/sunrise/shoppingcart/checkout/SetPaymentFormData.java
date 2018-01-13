package com.commercetools.sunrise.shoppingcart.checkout;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultSetPaymentFormData.class)
public interface SetPaymentFormData {

    String paymentMethod();
}
