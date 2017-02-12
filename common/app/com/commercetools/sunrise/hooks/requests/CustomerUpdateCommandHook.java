package com.commercetools.sunrise.hooks.requests;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;

public interface CustomerUpdateCommandHook extends RequestHook {

    CustomerUpdateCommand onCustomerUpdateCommand(final CustomerUpdateCommand customerUpdateCommand);

    static CustomerUpdateCommand runHook(final HookRunner hookRunner, final CustomerUpdateCommand customerUpdateCommand) {
        return hookRunner.runUnaryOperatorHook(CustomerUpdateCommandHook.class, CustomerUpdateCommandHook::onCustomerUpdateCommand, customerUpdateCommand);
    }
}
