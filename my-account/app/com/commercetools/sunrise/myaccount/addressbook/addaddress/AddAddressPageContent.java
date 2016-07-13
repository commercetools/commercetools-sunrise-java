package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormSettings;
import play.data.Form;

public class AddAddressPageContent extends PageContent {

    private Form<?> newAddressForm;
    private AddressFormSettings newAddressFormSettings;

    public Form<?> getNewAddressForm() {
        return newAddressForm;
    }

    public void setNewAddressForm(final Form<?> newAddressForm) {
        this.newAddressForm = newAddressForm;
    }

    public AddressFormSettings getNewAddressFormSettings() {
        return newAddressFormSettings;
    }

    public void setNewAddressFormSettings(final AddressFormSettings newAddressFormSettings) {
        this.newAddressFormSettings = newAddressFormSettings;
    }
}