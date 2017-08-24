package com.commercetools.sunrise.framework.hooks.ctprequests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerPasswordResetCommand;

/**
 * Hook for {@link CustomerPasswordResetCommand}.
 */
public interface CustomerPasswordResetCommandHook extends CtpRequestHook {

    CustomerPasswordResetCommand onCustomerPasswordResetCommand(
            final CustomerPasswordResetCommand customerPasswordResetCommand);

    static CustomerPasswordResetCommand runHook(final HookRunner hookRunner,
                                                      final CustomerPasswordResetCommand customerPasswordResetCommand) {
        return hookRunner.runUnaryOperatorHook(CustomerPasswordResetCommandHook.class,
                CustomerPasswordResetCommandHook::onCustomerPasswordResetCommand,
                customerPasswordResetCommand);
    }
}
