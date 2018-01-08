package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(DefaultMyPersonalDetailsControllerAction.class)
@FunctionalInterface
public interface MyPersonalDetailsControllerAction extends ControllerAction, Function<MyPersonalDetailsFormData, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final MyPersonalDetailsFormData formData);
}
