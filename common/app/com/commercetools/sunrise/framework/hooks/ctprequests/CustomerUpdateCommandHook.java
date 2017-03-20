package com.commercetools.sunrise.framework.hooks.ctprequests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;

public interface CustomerUpdateCommandHook extends CtpRequestHook {

    CustomerUpdateCommand onCustomerUpdateCommand(final CustomerUpdateCommand customerUpdateCommand);

    static CustomerUpdateCommand runHook(final HookRunner hookRunner, final CustomerUpdateCommand customerUpdateCommand) {
        return hookRunner.runUnaryOperatorHook(CustomerUpdateCommandHook.class, CustomerUpdateCommandHook::onCustomerUpdateCommand, customerUpdateCommand);
    }
}
