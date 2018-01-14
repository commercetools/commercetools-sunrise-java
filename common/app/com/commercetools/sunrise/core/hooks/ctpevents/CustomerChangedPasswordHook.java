package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.Customer;

public interface CustomerChangedPasswordHook extends CtpEventHook {

    void onCustomerChangedPassword(final Customer customer);

    static void runHook(final HookRunner hookRunner, final Customer customer) {
        hookRunner.runEventHook(CustomerChangedPasswordHook.class, hook -> hook.onCustomerChangedPassword(customer));
    }
}
