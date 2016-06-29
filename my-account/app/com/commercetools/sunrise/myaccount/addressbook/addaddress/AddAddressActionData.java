package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressActionData;
import io.sphere.sdk.customers.Customer;

import javax.annotation.Nullable;
import java.util.Optional;

public class AddAddressActionData extends AddressActionData {

    public AddAddressActionData(@Nullable final Customer customer) {
        super(customer);
    }

    @Override
    public Optional<Customer> customer() {
        return super.customer();
    }

    public static AddAddressActionData of(@Nullable final Customer customer) {
        return new AddAddressActionData(customer);
    }

    public static AddAddressActionData ofNotFoundCustomer() {
        return of(null);
    }
}
