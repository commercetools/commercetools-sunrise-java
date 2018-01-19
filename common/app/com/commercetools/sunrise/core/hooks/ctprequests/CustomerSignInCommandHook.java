package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface CustomerSignInCommandHook extends FilterHook {

    CompletionStage<CustomerSignInResult> on(CustomerSignInCommand request, Function<CustomerSignInCommand, CompletionStage<CustomerSignInResult>> nextComponent);

    static CompletionStage<CustomerSignInResult> run(final HookRunner hookRunner, final CustomerSignInCommand request, final Function<CustomerSignInCommand, CompletionStage<CustomerSignInResult>> execution) {
        return hookRunner.run(CustomerSignInCommandHook.class, request, execution, h -> h::on);
    }
}
