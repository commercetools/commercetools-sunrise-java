package com.commercetools.sunrise.models.addresses;

import com.commercetools.sunrise.core.SunriseModel;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

public final class AddressWithCustomer extends SunriseModel {

    private final Address address;
    private final Customer customer;

    private AddressWithCustomer(final Address address, final Customer customer) {
        this.address = address;
        this.customer = customer;
    }

    public Address getAddress() {
        return address;
    }

    public Customer getCustomer() {
        return customer;
    }

    public static AddressWithCustomer of(final Address address, final Customer customer) {
        return new AddressWithCustomer(address, customer);
    }
}
