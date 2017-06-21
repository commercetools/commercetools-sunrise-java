package com.commercetools.sunrise.shoppingcart.checkout.shipping.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

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
