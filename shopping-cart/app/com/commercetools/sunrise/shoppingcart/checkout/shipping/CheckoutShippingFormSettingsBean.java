package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.models.ModelBean;

public class CheckoutShippingFormSettingsBean extends ModelBean {

    private ShippingMethodFormFieldBean shippingMethod;

    public CheckoutShippingFormSettingsBean() {
    }

    public ShippingMethodFormFieldBean getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(final ShippingMethodFormFieldBean shippingMethod) {
        this.shippingMethod = shippingMethod;
    }
}
