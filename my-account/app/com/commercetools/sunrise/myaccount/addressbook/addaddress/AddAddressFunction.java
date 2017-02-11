package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultAddAddressFunction.class)
@FunctionalInterface
public interface AddAddressFunction extends BiFunction<Customer, AddressBookAddressFormData, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final Customer customer, final AddressBookAddressFormData formData);
}
