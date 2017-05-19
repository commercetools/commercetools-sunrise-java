package com.commercetools.sunrise.framework.hooks.ctprequests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerChangePasswordCommand;

public interface CustomerChangePasswordCommandHook extends CtpRequestHook {

    CustomerChangePasswordCommand onCustomerChangePasswordCommand(final CustomerChangePasswordCommand changePasswordCommand);

    static CustomerChangePasswordCommand runHook(final HookRunner hookRunner, final CustomerChangePasswordCommand changePasswordCommand) {
        return hookRunner.runUnaryOperatorHook(CustomerChangePasswordCommandHook.class, CustomerChangePasswordCommandHook::onCustomerChangePasswordCommand, changePasswordCommand);
    }
}
