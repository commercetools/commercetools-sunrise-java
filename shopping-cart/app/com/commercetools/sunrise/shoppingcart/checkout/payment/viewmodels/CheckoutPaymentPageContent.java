package com.commercetools.sunrise.shoppingcart.checkout.payment.viewmodels;

import com.commercetools.sunrise.shoppingcart.checkout.AbstractCheckoutPageContent;
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
