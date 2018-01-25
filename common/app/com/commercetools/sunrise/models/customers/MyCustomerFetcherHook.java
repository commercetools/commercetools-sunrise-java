package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface MyCustomerFetcherHook extends FilterHook {

    CompletionStage<Optional<Customer>> on(CustomerQuery request, Function<CustomerQuery, CompletionStage<Optional<Customer>>> nextComponent);
}
