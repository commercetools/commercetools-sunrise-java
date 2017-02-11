package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressWithCustomer;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultRemoveAddressFunction.class)
@FunctionalInterface
public interface RemoveAddressFunction extends BiFunction<RemoveAddressFormData, AddressWithCustomer, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final RemoveAddressFormData formData, final AddressWithCustomer actionData);
}
