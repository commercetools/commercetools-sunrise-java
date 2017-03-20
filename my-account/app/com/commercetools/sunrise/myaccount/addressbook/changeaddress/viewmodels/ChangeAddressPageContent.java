package com.commercetools.sunrise.myaccount.addressbook.changeaddress.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels.AddressFormSettingsViewModel;
import play.data.Form;

public class ChangeAddressPageContent extends PageContent {

    private Form<?> editAddressForm;
    private AddressFormSettingsViewModel editAddressFormSettings;

    public Form<?> getEditAddressForm() {
        return editAddressForm;
    }

    public void setEditAddressForm(final Form<?> editAddressForm) {
        this.editAddressForm = editAddressForm;
    }

    public AddressFormSettingsViewModel getEditAddressFormSettings() {
        return editAddressFormSettings;
    }

    public void setEditAddressFormSettings(final AddressFormSettingsViewModel editAddressFormSettings) {
        this.editAddressFormSettings = editAddressFormSettings;
    }
}