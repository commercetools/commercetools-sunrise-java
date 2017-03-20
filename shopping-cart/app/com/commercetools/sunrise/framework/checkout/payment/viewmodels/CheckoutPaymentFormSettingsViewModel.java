package com.commercetools.sunrise.framework.checkout.payment.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

public class CheckoutPaymentFormSettingsViewModel extends ViewModel {

    private PaymentMethodFormFieldViewModel paymentMethod;

    public CheckoutPaymentFormSettingsViewModel() {
    }

    public PaymentMethodFormFieldViewModel getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(final PaymentMethodFormFieldViewModel paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
