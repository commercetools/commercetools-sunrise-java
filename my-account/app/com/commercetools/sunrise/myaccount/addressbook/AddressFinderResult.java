package com.commercetools.sunrise.myaccount.addressbook;

import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;
import java.util.Optional;

public class AddressFinderResult {

    @Nullable
    private final Customer customer;
    @Nullable
    private final Address address;

    protected AddressFinderResult(@Nullable final Customer customer, @Nullable final Address address) {
        this.customer = customer;
        this.address = address;
    }

    public Optional<Customer> getCustomer() {
        return Optional.ofNullable(customer);
    }

    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    public static AddressFinderResult of(@Nullable final Customer customer, @Nullable final Address address) {
        return new AddressFinderResult(customer, address);
    }

    public static AddressFinderResult ofNotFoundCustomer() {
        return of(null, null);
    }
}
