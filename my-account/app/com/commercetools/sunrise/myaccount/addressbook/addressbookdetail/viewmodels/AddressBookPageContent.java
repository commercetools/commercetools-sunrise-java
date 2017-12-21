package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import io.sphere.sdk.models.Address;

import java.util.List;

public class AddressBookPageContent extends PageContent {

    private Address defaultShippingAddress;
    private Address defaultBillingAddress;
    private List<Address> addresses;

    public AddressBookPageContent() {
    }

    public Address getDefaultShippingAddress() {
        return defaultShippingAddress;
    }

    public void setDefaultShippingAddress(final Address defaultShippingAddress) {
        this.defaultShippingAddress = defaultShippingAddress;
    }

    public Address getDefaultBillingAddress() {
        return defaultBillingAddress;
    }

    public void setDefaultBillingAddress(final Address defaultBillingAddress) {
        this.defaultBillingAddress = defaultBillingAddress;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(final List<Address> addresses) {
        this.addresses = addresses;
    }
}