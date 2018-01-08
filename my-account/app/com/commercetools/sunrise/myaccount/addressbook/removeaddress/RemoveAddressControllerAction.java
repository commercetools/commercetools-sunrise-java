package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(DefaultRemoveAddressControllerAction.class)
@FunctionalInterface
public interface RemoveAddressControllerAction extends ControllerAction, Function<RemoveAddressFormData, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final RemoveAddressFormData formData);
}
