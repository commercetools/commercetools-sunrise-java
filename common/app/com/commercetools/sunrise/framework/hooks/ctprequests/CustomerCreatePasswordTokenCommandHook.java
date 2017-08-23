package com.commercetools.sunrise.framework.hooks.ctprequests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerCreatePasswordTokenCommand;

/**
 * Hook for {@link CustomerCreatePasswordTokenCommand}.
 */
public interface CustomerCreatePasswordTokenCommandHook extends CtpRequestHook {

    CustomerCreatePasswordTokenCommand onCustomerCreatePasswordTokenCommand(
            final CustomerCreatePasswordTokenCommand customerCreatePasswordTokenCommand);

    static CustomerCreatePasswordTokenCommand runHook(final HookRunner hookRunner,
                                                      final CustomerCreatePasswordTokenCommand customerCreatePasswordTokenCommand) {
        return hookRunner.runUnaryOperatorHook(CustomerCreatePasswordTokenCommandHook.class,
                CustomerCreatePasswordTokenCommandHook::onCustomerCreatePasswordTokenCommand,
                customerCreatePasswordTokenCommand);
    }
}
