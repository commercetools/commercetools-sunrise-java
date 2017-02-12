package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.AddressWithCustomer;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultChangeAddressExecutor.class)
@FunctionalInterface
public interface ChangeAddressExecutor extends BiFunction<AddressWithCustomer, AddressBookAddressFormData, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final AddressWithCustomer addressWithCustomer, final AddressBookAddressFormData formData);
}
