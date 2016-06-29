package com.commercetools.sunrise.myaccount.addressbook;

import io.sphere.sdk.customers.Customer;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AddressActionData {

    @Nullable
    private final Customer customer;

    protected AddressActionData(@Nullable final Customer customer) {
        this.customer = customer;
    }

    public Optional<Customer> customer() {
        return Optional.ofNullable(customer);
    }
}
