package com.commercetools.sunrise.shoppingcart.checkout.payment;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.Required;

public class DefaultCheckoutPaymentFormData extends Base implements CheckoutPaymentFormData {

    @Required
    private String payment;

    @Override
    public String paymentMethod() {
        return payment;
    }

    @Override
    public void applyPaymentMethod(final String paymentMethod) {
        this.payment = paymentMethod;
    }


    // Getters & setters

    public String getPayment() {
        return payment;
    }

    public void setPayment(final String payment) {
        this.payment = payment;
    }
}
