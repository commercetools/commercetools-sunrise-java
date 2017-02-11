package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultAddAddressExecutor.class)
public interface AddAddressExecutor {

    CompletionStage<Customer> addAddress(final Customer customer, final AddressBookAddressFormData formData);
}
