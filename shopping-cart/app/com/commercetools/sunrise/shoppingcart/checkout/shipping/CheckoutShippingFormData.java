package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class CheckoutShippingFormData extends Base implements CheckoutShippingFormDataLike {

    @Constraints.Required
    private String shippingMethodId;

    @Override
    public String getShippingMethodId() {
        return shippingMethodId;
    }

    public void setShippingMethodId(final String shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }
}
