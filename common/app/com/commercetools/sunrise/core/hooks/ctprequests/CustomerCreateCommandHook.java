package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;

import java.util.concurrent.CompletionStage;

public interface CustomerCreateCommandHook extends CtpRequestHook {

    CompletionStage<CustomerCreateCommand> onCustomerCreateCommand(final CustomerCreateCommand command);

    static CompletionStage<CustomerCreateCommand> runHook(final HookRunner hookRunner, final CustomerCreateCommand command) {
        return hookRunner.runActionHook(CustomerCreateCommandHook.class, CustomerCreateCommandHook::onCustomerCreateCommand, command);
    }
}
