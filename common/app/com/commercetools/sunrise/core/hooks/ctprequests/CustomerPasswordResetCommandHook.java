package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerPasswordResetCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Hook for {@link CustomerPasswordResetCommand}.
 */
public interface CustomerPasswordResetCommandHook extends FilterHook {

    CompletionStage<Customer> on(CustomerPasswordResetCommand request, Function<CustomerPasswordResetCommand, CompletionStage<Customer>> nextComponent);

    static CompletionStage<Customer> run(final HookRunner hookRunner, final CustomerPasswordResetCommand request, final Function<CustomerPasswordResetCommand, CompletionStage<Customer>> execution) {
        return hookRunner.run(CustomerPasswordResetCommandHook.class, request, execution, h -> h::on);
    }
}
