package com.commercetools.sunrise.shoppingcart.checkout.shipping;

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
