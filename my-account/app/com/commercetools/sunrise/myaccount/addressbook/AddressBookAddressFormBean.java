package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.common.forms.AddressFormBean;

public class AddressBookAddressFormBean extends AddressFormBean {

    private boolean defaultShippingAddress;
    private boolean defaultBillingAddress;

    public AddressBookAddressFormBean() {
    }

    public boolean isDefaultShippingAddress() {
        return defaultShippingAddress;
    }

    public void setDefaultShippingAddress(final boolean defaultShippingAddress) {
        this.defaultShippingAddress = defaultShippingAddress;
    }

    public boolean isDefaultBillingAddress() {
        return defaultBillingAddress;
    }

    public void setDefaultBillingAddress(final boolean defaultBillingAddress) {
        this.defaultBillingAddress = defaultBillingAddress;
    }
}
