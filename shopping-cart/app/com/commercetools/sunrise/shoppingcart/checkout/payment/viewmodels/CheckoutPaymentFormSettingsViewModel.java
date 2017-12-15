package com.commercetools.sunrise.shoppingcart.checkout.payment.viewmodels;

import com.commercetools.sunrise.core.viewmodels.ViewModel;

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
