package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.CustomerChangedPasswordHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.CustomerChangePasswordCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerChangePasswordCommand;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractCustomerChangePasswordExecutor extends AbstractSphereRequestExecutor {

    protected AbstractCustomerChangePasswordExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Customer> executeRequest(final CustomerChangePasswordCommand baseCommand) {
        final CustomerChangePasswordCommand command = CustomerChangePasswordCommandHook.runHook(getHookRunner(), baseCommand);
        return getSphereClient().execute(command)
                .thenApplyAsync(updatedCustomer -> {
                    CustomerChangedPasswordHook.runHook(getHookRunner(), updatedCustomer);
                    return updatedCustomer;
                }, HttpExecution.defaultContext());
    }
}
