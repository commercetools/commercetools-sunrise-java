package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.models.ViewModel;

public class CheckoutPaymentFormSettingsBean extends ViewModel {

    private PaymentMethodFormFieldBean paymentMethod;

    public CheckoutPaymentFormSettingsBean() {
    }

    public PaymentMethodFormFieldBean getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(final PaymentMethodFormFieldBean paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
