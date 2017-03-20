package com.commercetools.sunrise.myaccount.addressbook.addaddress.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels.AddressFormSettingsViewModel;
import play.data.Form;

public class AddAddressPageContent extends PageContent {

    private Form<?> newAddressForm;
    private AddressFormSettingsViewModel newAddressFormSettings;

    public Form<?> getNewAddressForm() {
        return newAddressForm;
    }

    public void setNewAddressForm(final Form<?> newAddressForm) {
        this.newAddressForm = newAddressForm;
    }

    public AddressFormSettingsViewModel getNewAddressFormSettings() {
        return newAddressFormSettings;
    }

    public void setNewAddressFormSettings(final AddressFormSettingsViewModel newAddressFormSettings) {
        this.newAddressFormSettings = newAddressFormSettings;
    }
}