package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultChangeAddressExecutor.class)
public interface ChangeAddressExecutor {

    CompletionStage<Customer> changeAddress(final Customer customer, final Address oldAddress, final AddressBookAddressFormData formData);
}
