package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerCreatePasswordTokenCommand;

import java.util.concurrent.CompletionStage;

/**
 * Hook for {@link CustomerCreatePasswordTokenCommand}.
 */
public interface CustomerCreatePasswordTokenCommandHook extends CtpRequestHook {

    CompletionStage<CustomerCreatePasswordTokenCommand> onCustomerCreatePasswordTokenCommand(final CustomerCreatePasswordTokenCommand command);

    static CompletionStage<CustomerCreatePasswordTokenCommand> runHook(final HookRunner hookRunner, final CustomerCreatePasswordTokenCommand command) {
        return hookRunner.runActionHook(CustomerCreatePasswordTokenCommandHook.class, CustomerCreatePasswordTokenCommandHook::onCustomerCreatePasswordTokenCommand, command);
    }
}
