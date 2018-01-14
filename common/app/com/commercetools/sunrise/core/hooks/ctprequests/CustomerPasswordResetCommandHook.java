package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerPasswordResetCommand;

import java.util.concurrent.CompletionStage;

/**
 * Hook for {@link CustomerPasswordResetCommand}.
 */
public interface CustomerPasswordResetCommandHook extends CtpRequestHook {

    CompletionStage<CustomerPasswordResetCommand> onCustomerPasswordResetCommand(final CustomerPasswordResetCommand command);

    static CompletionStage<CustomerPasswordResetCommand> runHook(final HookRunner hookRunner, final CustomerPasswordResetCommand command) {
        return hookRunner.runActionHook(CustomerPasswordResetCommandHook.class, CustomerPasswordResetCommandHook::onCustomerPasswordResetCommand, command);
    }
}
