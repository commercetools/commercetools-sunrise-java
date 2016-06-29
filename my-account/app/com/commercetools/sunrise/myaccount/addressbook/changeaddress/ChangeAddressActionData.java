package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressActionData;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;
import java.util.Optional;

public class ChangeAddressActionData extends AddressActionData {

    @Nullable
    private final Address oldAddress;

    protected ChangeAddressActionData(@Nullable final Customer customer, @Nullable final Address oldAddress) {
        super(customer);
        this.oldAddress = oldAddress;
    }

    @Override
    public Optional<Customer> customer() {
        return super.customer();
    }

    public Optional<Address> oldAddress() {
        return Optional.ofNullable(oldAddress);
    }

    public static ChangeAddressActionData of(@Nullable final Customer customer, @Nullable final Address oldAddress) {
        return new ChangeAddressActionData(customer, oldAddress);
    }

    public static ChangeAddressActionData ofNotFoundAddress(final Customer customer) {
        return of(customer, null);
    }

    public static ChangeAddressActionData ofNotFoundCustomer() {
        return of(null, null);
    }
}
