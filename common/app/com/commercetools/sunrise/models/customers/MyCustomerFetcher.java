package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.controllers.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@ImplementedBy(DefaultMyCustomerFetcher.class)
@FunctionalInterface
public interface MyCustomerFetcher extends ResourceFetcher<Customer>, Supplier<CompletionStage<Optional<Customer>>> {

    @Override
    CompletionStage<Optional<Customer>> get();
}
