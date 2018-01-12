package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(DefaultMyPersonalDetailsFormAction.class)
@FunctionalInterface
public interface MyPersonalDetailsFormAction extends FormAction, Function<MyPersonalDetailsFormData, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final MyPersonalDetailsFormData formData);
}
