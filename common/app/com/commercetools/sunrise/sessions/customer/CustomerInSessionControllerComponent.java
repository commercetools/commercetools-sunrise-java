package com.commercetools.sunrise.sessions.customer;

import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.CustomerLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.CustomerUpdatedHook;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerSignInResult;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class CustomerInSessionControllerComponent implements ControllerComponent, CustomerSignInResultLoadedHook, CustomerUpdatedHook, CustomerLoadedHook, PageDataReadyHook {

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

    @Override
    public void onPageDataReady(final PageData pageData) {
        customerInSession.findUserInfo()
                .ifPresent(pageData.getMeta()::setUser);
    }

    private void overwriteCustomerInSession(final Customer customer) {
        customerInSession.store(customer);
    }
}
