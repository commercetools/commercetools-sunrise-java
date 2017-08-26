package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.CustomerTokenCreatedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.CustomerCreatePasswordTokenCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerToken;
import io.sphere.sdk.customers.commands.CustomerCreatePasswordTokenCommand;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

/**
 * An abstract executor to create a customer password reset token {@link CustomerCreatePasswordTokenCommand}
 * and executes the registered hooks {@link CustomerCreatePasswordTokenCommandHook} and {@link CustomerTokenCreatedHook}.
 */
public abstract class AbstractCustomerCreatePasswordTokenExecutor extends AbstractSphereRequestExecutor {

    protected AbstractCustomerCreatePasswordTokenExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<CustomerToken> executeRequest(final CustomerCreatePasswordTokenCommand baseCommand) {
        final CustomerCreatePasswordTokenCommand command = CustomerCreatePasswordTokenCommandHook.runHook(getHookRunner(), baseCommand);
        return getSphereClient().execute(command).thenApplyAsync(customerToken -> {
            CustomerTokenCreatedHook.runHook(getHookRunner(), customerToken);
            return customerToken;
        }, HttpExecution.defaultContext());
    }
}
