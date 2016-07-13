package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.myaccount.common.MyAccountActionData;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

public class AddressBookActionData extends MyAccountActionData {

    private final Address address;

    public AddressBookActionData(final Customer customer, final Address address) {
        super(customer);
        this.address = address;
    }

    @Override
    public Customer getCustomer() {
        return super.getCustomer();
    }

    public Address getAddress() {
        return address;
    }
}
