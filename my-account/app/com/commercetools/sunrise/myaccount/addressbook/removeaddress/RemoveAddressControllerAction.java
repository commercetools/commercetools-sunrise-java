package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.commercetools.sunrise.framework.viewmodels.content.addresses.AddressWithCustomer;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultRemoveAddressControllerAction.class)
@FunctionalInterface
public interface RemoveAddressControllerAction extends ControllerAction, BiFunction<AddressWithCustomer, RemoveAddressFormData, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final AddressWithCustomer addressWithCustomer, final RemoveAddressFormData formData);
}
