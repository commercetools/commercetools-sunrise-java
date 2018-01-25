package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface MyCustomerCreatorHook extends FilterHook {

    CompletionStage<CustomerSignInResult> on(CustomerCreateCommand request, Function<CustomerCreateCommand, CompletionStage<CustomerSignInResult>> nextComponent);
}
