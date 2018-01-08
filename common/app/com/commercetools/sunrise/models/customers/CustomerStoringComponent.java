package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerLoadedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerUpdatedHook;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerSignInResult;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class CustomerStoringComponent implements ControllerComponent, CustomerSignInResultLoadedHook, CustomerUpdatedHook, CustomerLoadedHook {

    private final CustomerInSession customerInSession;
    private final MyCustomerInCache myCustomerInCache;

    @Inject
    CustomerStoringComponent(final CustomerInSession customerInSession, final MyCustomerInCache myCustomerInCache) {
        this.customerInSession = customerInSession;
        this.myCustomerInCache = myCustomerInCache;
    }

    @Override
    public CompletionStage<?> onCustomerSignInResultLoaded(final CustomerSignInResult customerSignInResult) {
        return storeCustomer(customerSignInResult.getCustomer());
    }

    @Override
    public CompletionStage<?> onCustomerUpdated(final Customer customer) {
        return storeCustomer(customer);
    }

    @Override
    public CompletionStage<?> onCustomerLoaded(final Customer customer) {
        return storeCustomer(customer);
    }

    private CompletionStage<?> storeCustomer(final Customer customer) {
        customerInSession.store(customer);
        myCustomerInCache.store(customer);
        return completedFuture(null);
    }
}
