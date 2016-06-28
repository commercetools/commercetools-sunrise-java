package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormBean;

public class ChangeAddressPageContent extends PageContent {

    private AddressBookAddressFormBean editAddressForm;

    public ChangeAddressPageContent() {
    }

    public AddressBookAddressFormBean getEditAddressForm() {
        return editAddressForm;
    }

    public void setEditAddressForm(final AddressBookAddressFormBean editAddressForm) {
        this.editAddressForm = editAddressForm;
    }
}