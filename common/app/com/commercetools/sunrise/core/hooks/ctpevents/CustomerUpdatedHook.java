package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.Customer;

/**
 * Hook called for components with read access to the customer. The customer should not be changed here.
 */
public interface CustomerUpdatedHook extends CtpEventHook {

    void onCustomerUpdated(final Customer customer);

    static void runHook(final HookRunner hookRunner, final Customer customer) {
        hookRunner.runEventHook(CustomerUpdatedHook.class, hook -> hook.onCustomerUpdated(customer));
    }
}
