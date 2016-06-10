package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class CheckoutShippingFormData extends Base {

    private String csrfToken;

    @Constraints.Required
    private String shippingMethodId;

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(final String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getShippingMethodId() {
        return shippingMethodId;
    }

    public void setShippingMethodId(final String shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }
}
