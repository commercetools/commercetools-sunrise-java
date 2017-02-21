package com.commercetools.sunrise.framework.checkout.shipping.viewmodels;

import com.commercetools.sunrise.common.models.ViewModel;

public class CheckoutShippingFormSettingsBean extends ViewModel {

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
