package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.CustomerToken;
import io.sphere.sdk.customers.commands.CustomerCreatePasswordTokenCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Hook for {@link CustomerCreatePasswordTokenCommand}.
 */
public interface CustomerCreatePasswordTokenCommandHook extends FilterHook {

    CompletionStage<CustomerToken> on(CustomerCreatePasswordTokenCommand request, Function<CustomerCreatePasswordTokenCommand, CompletionStage<CustomerToken>> nextComponent);

    static CompletionStage<CustomerToken> run(final HookRunner hookRunner, final CustomerCreatePasswordTokenCommand request, final Function<CustomerCreatePasswordTokenCommand, CompletionStage<CustomerToken>> execution) {
        return hookRunner.run(CustomerCreatePasswordTokenCommandHook.class, request, execution, h -> h::on);
    }
}
