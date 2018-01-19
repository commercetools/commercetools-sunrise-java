package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface CustomerCreateCommandHook extends FilterHook {

    CompletionStage<CustomerSignInResult> on(CustomerCreateCommand request, Function<CustomerCreateCommand, CompletionStage<CustomerSignInResult>> nextComponent);

    static CompletionStage<CustomerSignInResult> run(final HookRunner hookRunner, final CustomerCreateCommand request, final Function<CustomerCreateCommand, CompletionStage<CustomerSignInResult>> execution) {
        return hookRunner.run(CustomerCreateCommandHook.class, request, execution, h -> h::on);
    }
}
