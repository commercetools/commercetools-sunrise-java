package com.commercetools.sunrise.framework.hooks.ctprequests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;

public interface CustomerCreateCommandHook extends CtpRequestHook {

    CustomerCreateCommand onCustomerCreateCommand(final CustomerCreateCommand customerCreateCommand);

    static CustomerCreateCommand runHook(final HookRunner hookRunner, final CustomerCreateCommand customerCreateCommand) {
        return hookRunner.runUnaryOperatorHook(CustomerCreateCommandHook.class, CustomerCreateCommandHook::onCustomerCreateCommand, customerCreateCommand);
    }
}
