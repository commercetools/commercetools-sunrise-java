package com.commercetools.sunrise.myaccount;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@ImplementedBy(CustomerFinderBySession.class)
@FunctionalInterface
public interface CustomerFinder extends Supplier<CompletionStage<Optional<Customer>>> {

    @Override
    CompletionStage<Optional<Customer>> get();
}
