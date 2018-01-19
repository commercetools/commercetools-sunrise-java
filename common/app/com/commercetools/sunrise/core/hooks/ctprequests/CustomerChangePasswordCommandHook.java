package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerChangePasswordCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface CustomerChangePasswordCommandHook extends FilterHook {

    CompletionStage<Customer> on(CustomerChangePasswordCommand request, Function<CustomerChangePasswordCommand, CompletionStage<Customer>> nextComponent);

    static CompletionStage<Customer> run(final HookRunner hookRunner, final CustomerChangePasswordCommand request, final Function<CustomerChangePasswordCommand, CompletionStage<Customer>> execution) {
        return hookRunner.run(CustomerChangePasswordCommandHook.class, request, execution, h -> h::on);
    }
}
