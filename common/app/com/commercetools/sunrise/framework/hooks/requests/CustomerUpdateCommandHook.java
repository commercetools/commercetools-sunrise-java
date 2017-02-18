package com.commercetools.sunrise.framework.hooks.requests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;

public interface CustomerUpdateCommandHook extends RequestHook {

    CustomerUpdateCommand onCustomerUpdateCommand(final CustomerUpdateCommand customerUpdateCommand);

    static CustomerUpdateCommand runHook(final HookRunner hookRunner, final CustomerUpdateCommand customerUpdateCommand) {
        return hookRunner.runUnaryOperatorHook(CustomerUpdateCommandHook.class, CustomerUpdateCommandHook::onCustomerUpdateCommand, customerUpdateCommand);
    }
}
