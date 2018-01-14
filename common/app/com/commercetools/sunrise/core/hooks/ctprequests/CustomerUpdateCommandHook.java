package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;

import java.util.concurrent.CompletionStage;

public interface CustomerUpdateCommandHook extends CtpRequestHook {

    CompletionStage<CustomerUpdateCommand> onCustomerUpdateCommand(final CustomerUpdateCommand command);

    static CompletionStage<CustomerUpdateCommand> runHook(final HookRunner hookRunner, final CustomerUpdateCommand command) {
        return hookRunner.runActionHook(CustomerUpdateCommandHook.class, CustomerUpdateCommandHook::onCustomerUpdateCommand, command);
    }
}
