package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

public class DefaultCheckoutShippingFormData extends Base implements CheckoutShippingFormData {

    @Required
    @MinLength(1)
    private String shippingMethodId;

    @Override
    public String shippingMethod() {
        return shippingMethodId;
    }

    @Override
    public void applyShippingMethod(final String shippingMethod) {
        this.shippingMethodId = shippingMethod;
    }


    // Getters & setters

    public String getShippingMethodId() {
        return shippingMethodId;
    }

    public void setShippingMethodId(final String shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }
}
