package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultCheckoutPaymentFormData.class)
public interface CheckoutPaymentFormData {

    String paymentMethod();

    void applyPaymentMethod(final String paymentMethod);
}
