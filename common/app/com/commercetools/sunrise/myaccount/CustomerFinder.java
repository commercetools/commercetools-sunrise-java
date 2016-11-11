package com.commercetools.sunrise.myaccount;

import io.sphere.sdk.customers.Customer;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface CustomerFinder<C> {

    CompletionStage<Optional<Customer>> findCustomer(final C customerIdentifier);
}
