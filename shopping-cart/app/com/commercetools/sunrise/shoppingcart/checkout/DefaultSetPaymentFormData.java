package com.commercetools.sunrise.shoppingcart.checkout;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultSetPaymentFormData extends Base implements SetPaymentFormData {

    @Constraints.Required
    private String payment;

    @Override
    public String paymentMethod() {
        return payment;
    }
}
