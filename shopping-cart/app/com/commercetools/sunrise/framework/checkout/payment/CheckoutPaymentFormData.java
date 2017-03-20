package com.commercetools.sunrise.framework.checkout.payment;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultCheckoutPaymentFormData.class)
public interface CheckoutPaymentFormData {

    String paymentMethod();

    void applyPaymentMethod(final String paymentMethod);
}
