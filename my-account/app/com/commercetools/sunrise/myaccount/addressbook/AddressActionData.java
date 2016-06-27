package com.commercetools.sunrise.myaccount.addressbook;

import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AddressActionData<T extends AddressFormData> {

    @Nullable
    private final Customer customer;
    @Nullable
    private final Form<T> form;

    protected AddressActionData(@Nullable final Customer customer, @Nullable final Form<T> form) {
        this.customer = customer;
        this.form = form;
    }

    public Optional<Customer> customer() {
        return Optional.ofNullable(customer);
    }

    public Optional<Form<T>> form() {
        return Optional.ofNullable(form);
    }
}
