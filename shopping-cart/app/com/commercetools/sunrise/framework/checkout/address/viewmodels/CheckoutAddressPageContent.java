package com.commercetools.sunrise.framework.checkout.address.viewmodels;

import com.commercetools.sunrise.framework.checkout.AbstractCheckoutPageContent;
import play.data.Form;

public class CheckoutAddressPageContent extends AbstractCheckoutPageContent {

    private Form<?> addressForm;
    private CheckoutAddressFormSettingsBean addressFormSettings;

    public Form<?> getAddressForm() {
        return addressForm;
    }

    public void setAddressForm(final Form<?> addressForm) {
        this.addressForm = addressForm;
    }

    public CheckoutAddressFormSettingsBean getAddressFormSettings() {
        return addressFormSettings;
    }

    public void setAddressFormSettings(final CheckoutAddressFormSettingsBean addressFormSettings) {
        this.addressFormSettings = addressFormSettings;
    }
}
