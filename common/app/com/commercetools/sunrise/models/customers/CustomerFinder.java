package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@ImplementedBy(DefaultCustomerFinder.class)
@FunctionalInterface
public interface CustomerFinder extends ResourceFinder, Supplier<CompletionStage<Optional<Customer>>> {

    @Override
    CompletionStage<Optional<Customer>> get();
}
