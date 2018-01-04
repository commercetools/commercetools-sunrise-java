package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.sessions.ResourceInCache;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(CustomerInCacheImpl.class)
public interface CustomerInCache extends ResourceInCache<Customer> {

    @Override
    CompletionStage<Optional<Customer>> get();

    @Override
    void store(@Nullable final Customer cart);

    @Override
    void remove();
}
