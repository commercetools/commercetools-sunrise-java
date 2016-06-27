package com.commercetools.sunrise.shoppingcart.checkout.payment;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class CheckoutPaymentFormData extends Base {
    @Constraints.Required
    private String payment;

    public String getPayment() {
        return payment;
    }

    public void setPayment(final String payment) {
        this.payment = payment;
    }
}
