package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.commercetools.sunrise.models.addresses.AddressFormData;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(DefaultAddAddressControllerAction.class)
@FunctionalInterface
public interface AddAddressControllerAction extends ControllerAction, Function<AddressFormData, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final AddressFormData formData);
}
