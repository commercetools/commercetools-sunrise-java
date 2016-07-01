package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormSettings;
import play.data.Form;

public class ChangeAddressPageContent extends PageContent {

    private Form<?> editAddressForm;
    private AddressFormSettings editAddressFormSettings;

    public Form<?> getEditAddressForm() {
        return editAddressForm;
    }

    public void setEditAddressForm(final Form<?> editAddressForm) {
        this.editAddressForm = editAddressForm;
    }

    public AddressFormSettings getEditAddressFormSettings() {
        return editAddressFormSettings;
    }

    public void setEditAddressFormSettings(final AddressFormSettings editAddressFormSettings) {
        this.editAddressFormSettings = editAddressFormSettings;
    }
}