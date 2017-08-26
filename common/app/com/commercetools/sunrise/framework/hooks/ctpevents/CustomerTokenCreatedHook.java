package com.commercetools.sunrise.framework.hooks.ctpevents;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.customers.CustomerToken;

import java.util.concurrent.CompletionStage;

/**
 * This hook is called after a customer token was created.
 */
public interface CustomerTokenCreatedHook extends CtpEventHook {

    CompletionStage<?> onCustomerTokenCreated(CustomerToken customerToken);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final CustomerToken customerToken) {
        return hookRunner.runEventHook(CustomerTokenCreatedHook.class, hook -> hook.onCustomerTokenCreated(customerToken));
    }
}
