package com.commercetools.sunrise.shoppingcart.checkout.payment;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultCheckoutPaymentFormData extends Base implements CheckoutPaymentFormData {

    @Constraints.Required
    private String payment;

    @Override
    public String getPayment() {
        return payment;
    }

    @Override
    public void setPayment(final String payment) {
        this.payment = payment;
    }
}
