package com.commercetools.sunrise.myaccount;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(CustomerFinderBySession.class)
public interface CustomerFinder {

    CompletionStage<Optional<Customer>> findCustomer();

    CompletionStage<Optional<String>> findCustomerId();
}
