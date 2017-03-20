package com.commercetools.sunrise.framework.hooks.ctprequests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.customers.queries.CustomerQuery;

public interface CustomerQueryHook extends CtpRequestHook {

    CustomerQuery onCustomerQuery(final CustomerQuery customerQuery);

    static CustomerQuery runHook(final HookRunner hookRunner, final CustomerQuery customerQuery) {
        return hookRunner.runUnaryOperatorHook(CustomerQueryHook.class, CustomerQueryHook::onCustomerQuery, customerQuery);
    }
}
