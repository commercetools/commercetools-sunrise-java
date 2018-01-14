package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.commands.CustomerChangePasswordCommand;

import java.util.concurrent.CompletionStage;

public interface CustomerChangePasswordCommandHook extends CtpRequestHook {

    CompletionStage<CustomerChangePasswordCommand> onCustomerChangePasswordCommand(final CustomerChangePasswordCommand command);

    static CompletionStage<CustomerChangePasswordCommand> runHook(final HookRunner hookRunner, final CustomerChangePasswordCommand command) {
        return hookRunner.runActionHook(CustomerChangePasswordCommandHook.class, CustomerChangePasswordCommandHook::onCustomerChangePasswordCommand, command);
    }
}
