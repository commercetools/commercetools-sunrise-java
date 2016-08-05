package com.commercetools.sunrise.hooks.events;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;

public interface CustomerLoadedHook extends EventHook {

    CompletionStage<?> onCustomerLoaded(final Customer customer);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Customer customer) {
        return hookRunner.runEventHook(CustomerLoadedHook.class, hook -> hook.onCustomerLoaded(customer));
    }
}
