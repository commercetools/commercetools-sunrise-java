package com.commercetools.sunrise.framework.hooks.ctpevents;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;

public interface CustomerChangedPasswordHook extends CtpEventHook {

    CompletionStage<?> onCustomerChangedPassword(final Customer customer);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Customer customer) {
        return hookRunner.runEventHook(CustomerChangedPasswordHook.class, hook -> hook.onCustomerChangedPassword(customer));
    }
}
