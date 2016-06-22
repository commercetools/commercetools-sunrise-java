package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormBean;

public class AddAddressPageContent extends PageContent {

    private AddressBookAddressFormBean newAddressForm;

    public AddAddressPageContent() {
    }

    public AddressBookAddressFormBean getNewAddressForm() {
        return newAddressForm;
    }

    public void setNewAddressForm(final AddressBookAddressFormBean newAddressForm) {
        this.newAddressForm = newAddressForm;
    }
}