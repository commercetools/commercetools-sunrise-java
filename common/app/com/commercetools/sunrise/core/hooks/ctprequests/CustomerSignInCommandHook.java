package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;

public interface CustomerSignInCommandHook extends CtpRequestHook {

    CustomerSignInCommand onCustomerSignInCommand(final CustomerSignInCommand customerSignInCommand);

    static CustomerSignInCommand runHook(final HookRunner hookRunner, final CustomerSignInCommand customerSignInCommand) {
        return hookRunner.runUnaryOperatorHook(CustomerSignInCommandHook.class, CustomerSignInCommandHook::onCustomerSignInCommand, customerSignInCommand);
    }
}
