package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.sessions.ResourceInCache;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(MyCustomerInCacheImpl.class)
public interface MyCustomerInCache extends ResourceInCache<Customer> {

    @Override
    CompletionStage<Optional<Customer>> get();
}
