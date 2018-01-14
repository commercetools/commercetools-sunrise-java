package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;

import java.util.concurrent.CompletionStage;

public interface CustomerSignInCommandHook extends CtpRequestHook {

    CompletionStage<CustomerSignInCommand> onCustomerSignInCommand(final CustomerSignInCommand command);

    static CompletionStage<CustomerSignInCommand> runHook(final HookRunner hookRunner, final CustomerSignInCommand command) {
        return hookRunner.runActionHook(CustomerSignInCommandHook.class, CustomerSignInCommandHook::onCustomerSignInCommand, command);
    }
}
