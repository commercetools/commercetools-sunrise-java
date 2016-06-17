package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.common.pages.PageContent;

import java.util.List;

public class AddressBookPageContent extends PageContent {

    private AddressInfoBean defaultShippingAddress;
    private AddressInfoBean defaultBillingAddress;
    private List<AddressInfoBean> addresses;

    public AddressBookPageContent() {
    }

    public AddressInfoBean getDefaultShippingAddress() {
        return defaultShippingAddress;
    }

    public void setDefaultShippingAddress(final AddressInfoBean defaultShippingAddress) {
        this.defaultShippingAddress = defaultShippingAddress;
    }

    public AddressInfoBean getDefaultBillingAddress() {
        return defaultBillingAddress;
    }

    public void setDefaultBillingAddress(final AddressInfoBean defaultBillingAddress) {
        this.defaultBillingAddress = defaultBillingAddress;
    }

    public List<AddressInfoBean> getAddresses() {
        return addresses;
    }

    public void setAddresses(final List<AddressInfoBean> addresses) {
        this.addresses = addresses;
    }
}