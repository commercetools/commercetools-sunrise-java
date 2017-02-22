package com.commercetools.sunrise.framework.checkout.shipping.viewmodels;

import com.commercetools.sunrise.common.models.ViewModel;

public class CheckoutShippingFormSettingsViewModel extends ViewModel {

    private ShippingMethodFormFieldViewModel shippingMethod;

    public CheckoutShippingFormSettingsViewModel() {
    }

    public ShippingMethodFormFieldViewModel getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(final ShippingMethodFormFieldViewModel shippingMethod) {
        this.shippingMethod = shippingMethod;
    }
}
