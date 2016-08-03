package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.forms.FormBean;

public class CheckoutShippingFormSettingsBean extends FormBean {

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
