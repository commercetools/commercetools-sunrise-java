package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.shoppingcart.common.CheckoutPageContent;

public class CheckoutPaymentPageContent extends CheckoutPageContent {

    private CheckoutPaymentFormBean paymentForm;

    public CheckoutPaymentPageContent() {
    }

    public CheckoutPaymentFormBean getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(final CheckoutPaymentFormBean paymentForm) {
        this.paymentForm = paymentForm;
    }
}
