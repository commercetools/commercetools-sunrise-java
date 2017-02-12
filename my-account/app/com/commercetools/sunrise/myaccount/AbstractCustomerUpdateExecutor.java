package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.common.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.hooks.events.CustomerUpdatedHook;
import com.commercetools.sunrise.hooks.requests.CustomerUpdateCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractCustomerUpdateExecutor extends AbstractSphereRequestExecutor {

    protected AbstractCustomerUpdateExecutor(final SphereClient sphereClient, final HookContext hookContext) {
        super(sphereClient, hookContext);
    }

    protected final CompletionStage<Customer> executeRequest(final Customer customer, final CustomerUpdateCommand baseCommand) {
        final CustomerUpdateCommand command = CustomerUpdateCommandHook.runHook(getHookContext(), baseCommand);
        if (!command.getUpdateActions().isEmpty()) {
            return getSphereClient().execute(command)
                    .thenApplyAsync(updatedCustomer -> {
                        CustomerUpdatedHook.runHook(getHookContext(), updatedCustomer);
                        return updatedCustomer;
                    }, HttpExecution.defaultContext());
        } else {
            return completedFuture(customer);
        }
    }
}
