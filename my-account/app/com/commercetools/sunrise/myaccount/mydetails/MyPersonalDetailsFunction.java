package com.commercetools.sunrise.myaccount.mydetails;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultMyPersonalDetailsFunction.class)
@FunctionalInterface
public interface MyPersonalDetailsFunction extends BiFunction<Customer, MyPersonalDetailsFormData, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final Customer customer, final MyPersonalDetailsFormData formData);
}
