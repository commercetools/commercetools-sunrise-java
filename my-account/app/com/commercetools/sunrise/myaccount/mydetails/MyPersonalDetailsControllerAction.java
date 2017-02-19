package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultMyPersonalDetailsControllerAction.class)
@FunctionalInterface
public interface MyPersonalDetailsControllerAction extends ControllerAction, BiFunction<Customer, MyPersonalDetailsFormData, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final Customer customer, final MyPersonalDetailsFormData formData);
}
