package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormData;
import com.commercetools.sunrise.framework.viewmodels.content.addresses.AddressWithCustomer;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultChangeAddressControllerAction.class)
@FunctionalInterface
public interface ChangeAddressControllerAction extends ControllerAction, BiFunction<AddressWithCustomer, AddressFormData, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final AddressWithCustomer addressWithCustomer, final AddressFormData formData);
}
