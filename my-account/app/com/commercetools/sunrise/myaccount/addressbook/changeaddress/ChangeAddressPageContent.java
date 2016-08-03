package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.models.AddressFormSettingsBean;
import play.data.Form;

public class ChangeAddressPageContent extends PageContent {

    private Form<?> editAddressForm;
    private AddressFormSettingsBean editAddressFormSettings;

    public Form<?> getEditAddressForm() {
        return editAddressForm;
    }

    public void setEditAddressForm(final Form<?> editAddressForm) {
        this.editAddressForm = editAddressForm;
    }

    public AddressFormSettingsBean getEditAddressFormSettings() {
        return editAddressFormSettings;
    }

    public void setEditAddressFormSettings(final AddressFormSettingsBean editAddressFormSettings) {
        this.editAddressFormSettings = editAddressFormSettings;
    }
}