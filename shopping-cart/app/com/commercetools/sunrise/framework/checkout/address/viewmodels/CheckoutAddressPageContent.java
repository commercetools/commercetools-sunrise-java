package com.commercetools.sunrise.framework.checkout.address.viewmodels;

import com.commercetools.sunrise.framework.checkout.AbstractCheckoutPageContent;
import play.data.Form;

public class CheckoutAddressPageContent extends AbstractCheckoutPageContent {

    private Form<?> addressForm;
    private CheckoutAddressFormSettingsViewModel addressFormSettings;

    public Form<?> getAddressForm() {
        return addressForm;
    }

    public void setAddressForm(final Form<?> addressForm) {
        this.addressForm = addressForm;
    }

    public CheckoutAddressFormSettingsViewModel getAddressFormSettings() {
        return addressFormSettings;
    }

    public void setAddressFormSettings(final CheckoutAddressFormSettingsViewModel addressFormSettings) {
        this.addressFormSettings = addressFormSettings;
    }
}
