package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressWithCustomer;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultRemoveAddressExecutor.class)
@FunctionalInterface
public interface RemoveAddressExecutor extends BiFunction<AddressWithCustomer, RemoveAddressFormData, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final AddressWithCustomer addressWithCustomer, final RemoveAddressFormData formData);
}
