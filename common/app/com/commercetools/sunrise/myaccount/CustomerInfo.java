package com.commercetools.sunrise.myaccount;

import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import java.util.Optional;

public final class CustomerInfo extends Base {

    private final String id;
    private final String email;
    @Nullable
    private final String name;

    private CustomerInfo(final String id, final String email, @Nullable final String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public static CustomerInfo of(final String id, final String email, @Nullable final String name) {
        return new CustomerInfo(id, email, name);
    }

    public static CustomerInfo of(final Customer customer) {
        return new CustomerInfo(customer.getId(), customer.getEmail(), customer.getFirstName());
    }
}
