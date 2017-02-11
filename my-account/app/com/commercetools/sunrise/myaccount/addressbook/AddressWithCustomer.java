package com.commercetools.sunrise.myaccount.addressbook;

import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

public class AddressWithCustomer {

    private final Address address;
    private final Customer customer;

    public AddressWithCustomer(final Customer customer, final Address address) {
        this.address = address;
        this.customer = customer;
    }

    public Address getAddress() {
        return address;
    }

    public Customer getCustomer() {
        return customer;
    }
}
