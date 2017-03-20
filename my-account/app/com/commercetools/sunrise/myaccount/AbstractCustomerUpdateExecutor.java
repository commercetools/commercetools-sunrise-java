package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.CustomerUpdatedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.CustomerUpdateCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractCustomerUpdateExecutor extends AbstractSphereRequestExecutor {

    protected AbstractCustomerUpdateExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Customer> executeRequest(final Customer customer, final CustomerUpdateCommand baseCommand) {
        final CustomerUpdateCommand command = CustomerUpdateCommandHook.runHook(getHookRunner(), baseCommand);
        if (!command.getUpdateActions().isEmpty()) {
            return getSphereClient().execute(command)
                    .thenApplyAsync(updatedCustomer -> {
                        CustomerUpdatedHook.runHook(getHookRunner(), updatedCustomer);
                        return updatedCustomer;
                    }, HttpExecution.defaultContext());
        } else {
            return completedFuture(customer);
        }
    }
}
