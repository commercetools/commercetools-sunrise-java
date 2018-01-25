package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface MyCustomerUpdaterHook extends FilterHook {

    CompletionStage<Customer> on(CustomerUpdateCommand request, Function<CustomerUpdateCommand, CompletionStage<Customer>> nextComponent);
}
