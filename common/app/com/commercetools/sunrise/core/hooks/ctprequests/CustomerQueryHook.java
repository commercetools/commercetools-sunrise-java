package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.queries.CustomerQuery;

import java.util.concurrent.CompletionStage;

public interface CustomerQueryHook extends CtpRequestHook {

    CompletionStage<CustomerQuery> onCustomerQuery(final CustomerQuery query);

    static CompletionStage<CustomerQuery> runHook(final HookRunner hookRunner, final CustomerQuery query) {
        return hookRunner.runActionHook(CustomerQueryHook.class, CustomerQueryHook::onCustomerQuery, query);
    }
}
