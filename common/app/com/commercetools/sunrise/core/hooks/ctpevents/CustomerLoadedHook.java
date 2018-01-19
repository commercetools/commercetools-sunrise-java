package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.Customer;

@FunctionalInterface
public interface CustomerLoadedHook extends ConsumerHook {

    void onLoaded(Customer customer);

    static void run(final HookRunner hookRunner, final Customer resource) {
        hookRunner.run(CustomerLoadedHook.class, h -> h.onLoaded(resource));
    }
}
