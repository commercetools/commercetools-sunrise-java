package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.sessions.ResourceInCache;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.client.NotFoundException;
import io.sphere.sdk.customers.Customer;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(MyCustomerInCacheImpl.class)
public interface MyCustomerInCache extends ResourceInCache<Customer> {

    @Override
    CompletionStage<Optional<Customer>> get();

    default CompletionStage<Customer> require() {
        return get().thenApply(customer -> customer.orElseThrow(NotFoundException::new));
    }

    @Override
    void store(@Nullable final Customer cart);

    @Override
    void remove();
}
