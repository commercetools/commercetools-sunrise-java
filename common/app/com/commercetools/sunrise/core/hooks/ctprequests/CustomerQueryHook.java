package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface CustomerQueryHook extends FilterHook {

    CompletionStage<PagedQueryResult<Customer>> on(CustomerQuery request, Function<CustomerQuery, CompletionStage<PagedQueryResult<Customer>>> nextComponent);

    static CompletionStage<PagedQueryResult<Customer>> run(final HookRunner hookRunner, final CustomerQuery request, final Function<CustomerQuery, CompletionStage<PagedQueryResult<Customer>>> execution) {
        return hookRunner.run(CustomerQueryHook.class, request, execution, h -> h::on);
    }
}
