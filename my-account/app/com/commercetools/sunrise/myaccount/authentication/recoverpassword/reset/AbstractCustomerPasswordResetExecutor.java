package com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctprequests.CustomerPasswordResetCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerPasswordResetCommand;

import java.util.concurrent.CompletionStage;

/**
 * An abstract executor to create a reset a customers password.
 *
 * @see CustomerPasswordResetCommand
 */
public abstract class AbstractCustomerPasswordResetExecutor extends AbstractSphereRequestExecutor {

    protected AbstractCustomerPasswordResetExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Customer> executeRequest(final CustomerPasswordResetCommand baseCommand) {
        final CustomerPasswordResetCommand command = CustomerPasswordResetCommandHook.runHook(getHookRunner(), baseCommand);
        return getSphereClient().execute(command);
    }
}
