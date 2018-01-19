package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.sessions.AbstractUserResourceInCache;
import io.sphere.sdk.customers.Customer;
import play.cache.CacheApi;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@RequestScoped
final class MyCustomerImpl extends AbstractUserResourceInCache<Customer> implements MyCustomer {

    private final MyCustomerFetcher customerFinder;

    @Inject
    MyCustomerImpl(final MyCustomerInSession myCustomerInSession, final CacheApi cacheApi, final MyCustomerFetcher customerFinder) {
        super(myCustomerInSession, cacheApi);
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
