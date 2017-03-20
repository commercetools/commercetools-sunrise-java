package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.CustomerLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.CustomerQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public abstract class AbstractSingleCustomerQueryExecutor extends AbstractSphereRequestExecutor {

    @Inject
    protected AbstractSingleCustomerQueryExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Optional<Customer>> executeRequest(final CustomerQuery baseQuery) {
        final CustomerQuery request = CustomerQueryHook.runHook(getHookRunner(), baseQuery);
        return getSphereClient().execute(request)
                .thenApply(PagedQueryResult::head)
                .thenApplyAsync(customerOpt -> {
                    customerOpt.ifPresent(customer -> CustomerLoadedHook.runHook(getHookRunner(), customer));
                    return customerOpt;
                }, HttpExecution.defaultContext());
    }
}
