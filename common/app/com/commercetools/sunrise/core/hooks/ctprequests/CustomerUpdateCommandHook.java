package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface CustomerUpdateCommandHook extends FilterHook {

    CompletionStage<Customer> on(CustomerUpdateCommand request, Function<CustomerUpdateCommand, CompletionStage<Customer>> nextComponent);

    static CompletionStage<Customer> run(final HookRunner hookRunner, final CustomerUpdateCommand request, final Function<CustomerUpdateCommand, CompletionStage<Customer>> execution) {
        return hookRunner.run(CustomerUpdateCommandHook.class, request, execution, h -> h::on);
    }
}
