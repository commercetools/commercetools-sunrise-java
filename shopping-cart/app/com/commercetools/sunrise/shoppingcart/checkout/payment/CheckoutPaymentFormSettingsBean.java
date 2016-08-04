package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.forms.FormBean;

public class CheckoutPaymentFormSettingsBean extends FormBean {

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
