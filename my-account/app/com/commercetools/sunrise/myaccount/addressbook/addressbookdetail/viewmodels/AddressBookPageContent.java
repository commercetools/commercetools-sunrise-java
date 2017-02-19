package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.common.pages.PageContent;

import java.util.List;

public class AddressBookPageContent extends PageContent {

    private EditableAddressBean defaultShippingAddress;
    private EditableAddressBean defaultBillingAddress;
    private List<EditableAddressBean> addresses;

    public AddressBookPageContent() {
    }

    public EditableAddressBean getDefaultShippingAddress() {
        return defaultShippingAddress;
    }

    public void setDefaultShippingAddress(final EditableAddressBean defaultShippingAddress) {
        this.defaultShippingAddress = defaultShippingAddress;
    }

    public EditableAddressBean getDefaultBillingAddress() {
        return defaultBillingAddress;
    }

    public void setDefaultBillingAddress(final EditableAddressBean defaultBillingAddress) {
        this.defaultBillingAddress = defaultBillingAddress;
    }

    public List<EditableAddressBean> getAddresses() {
        return addresses;
    }

    public void setAddresses(final List<EditableAddressBean> addresses) {
        this.addresses = addresses;
    }
}