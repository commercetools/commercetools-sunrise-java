package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.commercetools.sunrise.core.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyCustomerFetcher.class)
public interface MyCustomerFetcher extends ResourceFetcher {

    CompletionStage<Optional<Customer>> get();

    default CompletionStage<Customer> require() {
        return get().thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
