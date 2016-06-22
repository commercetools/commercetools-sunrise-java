package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressActionData;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormData;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.annotation.Nullable;
import java.util.Optional;

public class AddAddressActionData<T extends AddressFormData> extends AddressActionData<T> {

    public AddAddressActionData(@Nullable final Customer customer, @Nullable final Form<T> form) {
        super(customer, form);
    }

    @Override
    public Optional<Customer> getCustomer() {
        return super.getCustomer();
    }

    @Override
    public Optional<Form<T>> getForm() {
        return super.getForm();
    }

    public static <T extends AddressFormData> AddAddressActionData<T> of(@Nullable final Customer customer, @Nullable final Form<T> form) {
        return new AddAddressActionData<>(customer, form);
    }

    public static AddAddressActionData ofNotFoundCustomer() {
        return of(null, null);
    }
}
