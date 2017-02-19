package com.commercetools.sunrise.myaccount.addressbook.addaddress.viewmodels;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels.AddressFormSettingsBean;
import play.data.Form;

public class AddAddressPageContent extends PageContent {

    private Form<?> newAddressForm;
    private AddressFormSettingsBean newAddressFormSettings;

    public Form<?> getNewAddressForm() {
        return newAddressForm;
    }

    public void setNewAddressForm(final Form<?> newAddressForm) {
        this.newAddressForm = newAddressForm;
    }

    public AddressFormSettingsBean getNewAddressFormSettings() {
        return newAddressFormSettings;
    }

    public void setNewAddressFormSettings(final AddressFormSettingsBean newAddressFormSettings) {
        this.newAddressFormSettings = newAddressFormSettings;
    }
}