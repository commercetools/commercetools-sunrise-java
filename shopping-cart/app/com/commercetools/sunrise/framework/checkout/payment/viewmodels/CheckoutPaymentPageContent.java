package com.commercetools.sunrise.framework.checkout.payment.viewmodels;

import com.commercetools.sunrise.framework.checkout.AbstractCheckoutPageContent;
import play.data.Form;

public class CheckoutPaymentPageContent extends AbstractCheckoutPageContent {

    private Form<?> paymentForm;
    private CheckoutPaymentFormSettingsViewModel paymentFormSettings;

    public CheckoutPaymentPageContent() {
    }

    public Form<?> getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(final Form<?> paymentForm) {
        this.paymentForm = paymentForm;
    }

    public CheckoutPaymentFormSettingsViewModel getPaymentFormSettings() {
        return paymentFormSettings;
    }

    public void setPaymentFormSettings(final CheckoutPaymentFormSettingsViewModel paymentFormSettings) {
        this.paymentFormSettings = paymentFormSettings;
    }
}
