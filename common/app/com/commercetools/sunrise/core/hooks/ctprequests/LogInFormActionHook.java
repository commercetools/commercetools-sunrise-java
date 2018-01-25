package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface LogInFormActionHook extends FilterHook {

    CompletionStage<CustomerSignInResult> on(CustomerSignInCommand request, Function<CustomerSignInCommand, CompletionStage<CustomerSignInResult>> nextComponent);
}
