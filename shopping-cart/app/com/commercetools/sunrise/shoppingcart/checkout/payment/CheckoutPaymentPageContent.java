package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.shoppingcart.common.CheckoutPageContent;
import play.data.Form;

public class CheckoutPaymentPageContent extends CheckoutPageContent {

    private Form<?> paymentForm;
    private CheckoutPaymentFormSettingsBean paymentFormSettings;

    public CheckoutPaymentPageContent() {
    }

    public Form<?> getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(final Form<?> paymentForm) {
        this.paymentForm = paymentForm;
    }

    public CheckoutPaymentFormSettingsBean getPaymentFormSettings() {
        return paymentFormSettings;
    }

    public void setPaymentFormSettings(final CheckoutPaymentFormSettingsBean paymentFormSettings) {
        this.paymentFormSettings = paymentFormSettings;
    }
}
