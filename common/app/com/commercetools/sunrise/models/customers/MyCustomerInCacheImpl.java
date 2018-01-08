package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.sessions.AbstractResourceInCache;
import io.sphere.sdk.customers.Customer;
import play.cache.CacheApi;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@RequestScoped
final class MyCustomerInCacheImpl extends AbstractResourceInCache<Customer> implements MyCustomerInCache {

    private final MyCustomerFetcher customerFinder;

    @Inject
    MyCustomerInCacheImpl(final CustomerInSession customerInSession, final CacheApi cacheApi, final MyCustomerFetcher customerFinder) {
        super(customerInSession, cacheApi);
        this.customerFinder = customerFinder;
    }

    @Override
    protected CompletionStage<Optional<Customer>> fetchResource() {
        return customerFinder.get();
    }

    @Override
    protected String generateCacheKey(final String customerId) {
        return "customer_" + customerId;
    }
}
