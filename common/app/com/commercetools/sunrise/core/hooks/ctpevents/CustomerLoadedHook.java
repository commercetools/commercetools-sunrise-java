package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.Customer;

public interface CustomerLoadedHook extends CtpEventHook {

    void onCustomerLoaded(final Customer customer);

    static void runHook(final HookRunner hookRunner, final Customer customer) {
        hookRunner.runEventHook(CustomerLoadedHook.class, hook -> hook.onCustomerLoaded(customer));
    }
}
