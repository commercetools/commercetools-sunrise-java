package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;

import java.util.List;

public class AddressBookPageContent extends PageContent {

    private EditableAddressViewModel defaultShippingAddress;
    private EditableAddressViewModel defaultBillingAddress;
    private List<EditableAddressViewModel> addresses;

    public AddressBookPageContent() {
    }

    public EditableAddressViewModel getDefaultShippingAddress() {
        return defaultShippingAddress;
    }

    public void setDefaultShippingAddress(final EditableAddressViewModel defaultShippingAddress) {
        this.defaultShippingAddress = defaultShippingAddress;
    }

    public EditableAddressViewModel getDefaultBillingAddress() {
        return defaultBillingAddress;
    }

    public void setDefaultBillingAddress(final EditableAddressViewModel defaultBillingAddress) {
        this.defaultBillingAddress = defaultBillingAddress;
    }

    public List<EditableAddressViewModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(final List<EditableAddressViewModel> addresses) {
        this.addresses = addresses;
    }
}