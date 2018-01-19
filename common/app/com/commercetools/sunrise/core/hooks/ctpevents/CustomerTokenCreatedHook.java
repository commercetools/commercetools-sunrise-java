package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.CustomerToken;

@FunctionalInterface
public interface CustomerTokenCreatedHook extends ConsumerHook {

    void onCreated(CustomerToken customerToken);

    static void run(final HookRunner hookRunner, final CustomerToken resource) {
        hookRunner.run(CustomerTokenCreatedHook.class, h -> h.onCreated(resource));
    }
}
