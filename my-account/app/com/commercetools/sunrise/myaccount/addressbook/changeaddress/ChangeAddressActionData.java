package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressActionData;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormData;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.data.Form;

import javax.annotation.Nullable;
import java.util.Optional;

public class ChangeAddressActionData<T extends AddressFormData> extends AddressActionData<T> {

    @Nullable
    private final Address oldAddress;

    protected ChangeAddressActionData(@Nullable final Customer customer, @Nullable final Form<T> form, @Nullable final Address oldAddress) {
        super(customer, form);
        this.oldAddress = oldAddress;
    }

    @Override
    public Optional<Customer> customer() {
        return super.customer();
    }

    @Override
    public Optional<Form<T>> form() {
        return super.form();
    }

    public Optional<Address> oldAddress() {
        return Optional.ofNullable(oldAddress);
    }

    public static <T extends AddressFormData> ChangeAddressActionData of(@Nullable final Customer customer,
                                                                         @Nullable final Form<T> form,
                                                                         @Nullable final Address oldAddress) {
        return new ChangeAddressActionData<>(customer, form, oldAddress);
    }

    public static ChangeAddressActionData ofNotFoundAddress(final Customer customer) {
        return new ChangeAddressActionData<>(customer, null, null);
    }

    public static ChangeAddressActionData ofNotFoundCustomer() {
        return of(null, null, null);
    }
}
