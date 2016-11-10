package com.commercetools.sunrise.hooks.events;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;

/**
 * Hook called for components with read access to the customer. The customer should not be changed here.
 */
public interface CustomerUpdatedHook extends EventHook {

    CompletionStage<?> onCustomerUpdated(final Customer customer);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Customer customer) {
        return hookRunner.runEventHook(CustomerUpdatedHook.class, hook -> hook.onCustomerUpdated(customer));
    }
}
