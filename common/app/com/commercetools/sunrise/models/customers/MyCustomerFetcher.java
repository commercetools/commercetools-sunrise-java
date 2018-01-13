package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.commercetools.sunrise.core.SingleResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyCustomerFetcher.class)
public interface MyCustomerFetcher extends SingleResourceFetcher<Customer, CustomerQuery> {

    Optional<CustomerQuery> defaultRequest();

    CompletionStage<Optional<Customer>> get();

    default CompletionStage<Customer> require() {
        return get().thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
