package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.controllers.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@ImplementedBy(DefaultCustomerFetcher.class)
@FunctionalInterface
public interface CustomerFetcher extends ResourceFetcher<Customer>, Supplier<CompletionStage<Optional<Customer>>> {

    @Override
    CompletionStage<Optional<Customer>> get();
}
