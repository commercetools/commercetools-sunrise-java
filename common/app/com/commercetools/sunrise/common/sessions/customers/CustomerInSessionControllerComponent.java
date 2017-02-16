package com.commercetools.sunrise.common.sessions.customers;

import com.commercetools.sunrise.common.injection.RequestScoped;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.events.CustomerLoadedHook;
import com.commercetools.sunrise.hooks.events.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.hooks.events.CustomerUpdatedHook;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerSignInResult;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public final class CustomerInSessionControllerComponent implements ControllerComponent, CustomerSignInResultLoadedHook, CustomerUpdatedHook, CustomerLoadedHook {

    private final CustomerInSession customerInSession;

    @Inject
    CustomerInSessionControllerComponent(final CustomerInSession customerInSession) {
        this.customerInSession = customerInSession;
    }

    @Override
    public CompletionStage<?> onCustomerSignInResultLoaded(final CustomerSignInResult customerSignInResult) {
        overwriteCustomerInSession(customerSignInResult.getCustomer());
        return completedFuture(null);
    }

    @Override
    public CompletionStage<?> onCustomerUpdated(final Customer customer) {
        overwriteCustomerInSession(customer);
        return completedFuture(null);
    }

    @Override
    public CompletionStage<?> onCustomerLoaded(final Customer customer) {
        overwriteCustomerInSession(customer);
        return completedFuture(null);
    }

    private void overwriteCustomerInSession(final Customer customer) {
        customerInSession.store(customer);
    }
}
