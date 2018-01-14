package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerLoadedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerUpdatedHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerSignInResult;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class CustomerComponent implements ControllerComponent, CustomerSignInResultLoadedHook, CustomerUpdatedHook, CustomerLoadedHook, PageDataHook {

    private final MyCustomerInSession myCustomerInSession;
    private final MyCustomerInCache myCustomerInCache;

    @Inject
    CustomerComponent(final MyCustomerInSession myCustomerInSession, final MyCustomerInCache myCustomerInCache) {
        this.myCustomerInSession = myCustomerInSession;
        this.myCustomerInCache = myCustomerInCache;
    }

    @Override
    public void onCustomerSignInResultLoaded(final CustomerSignInResult customerSignInResult) {
        storeCustomer(customerSignInResult.getCustomer());
    }

    @Override
    public void onCustomerUpdated(final Customer customer) {
        storeCustomer(customer);
    }

    @Override
    public void onCustomerLoaded(final Customer customer) {
        storeCustomer(customer);
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return myCustomerInCache.get()
                .thenApply(customerOpt -> customerOpt
                        .map(customer -> pageData.put("customer", customer))
                        .orElse(pageData));
    }

    private void storeCustomer(final Customer customer) {
        myCustomerInSession.store(customer);
        myCustomerInCache.store(customer);
    }
}
