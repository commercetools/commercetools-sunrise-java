package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.data.Form;

import javax.annotation.Nullable;
import java.util.Optional;

public class RemoveAddressActionData<T> {

    @Nullable
    private final Customer customer;
    @Nullable
    private final Form<T> form;
    @Nullable
    private final Address address;

    protected RemoveAddressActionData(@Nullable final Customer customer, @Nullable final Form<T> form, @Nullable final Address address) {
        this.customer = customer;
        this.form = form;
        this.address = address;
    }

    public Optional<Customer> getCustomer() {
        return Optional.ofNullable(customer);
    }

    public Optional<Form<T>> getForm() {
        return Optional.ofNullable(form);
    }

    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    public static <T extends RemoveAddressFormData> RemoveAddressActionData of(@Nullable final Customer customer,
                                                                               @Nullable final Form<T> form,
                                                                               @Nullable final Address oldAddress) {
        return new RemoveAddressActionData<>(customer, form, oldAddress);
    }

    public static RemoveAddressActionData ofNotFoundAddress(final Customer customer) {
        return new RemoveAddressActionData<>(customer, null, null);
    }

    public static RemoveAddressActionData ofNotFoundCustomer() {
        return of(null, null, null);
    }
}
