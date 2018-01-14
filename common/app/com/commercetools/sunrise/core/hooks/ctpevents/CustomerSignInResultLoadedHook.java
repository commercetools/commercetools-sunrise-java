package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.CustomerSignInResult;

import java.util.concurrent.CompletionStage;

public interface CustomerSignInResultLoadedHook extends CtpEventHook {

    void onCustomerSignInResultLoaded(final CustomerSignInResult customerSignInResult);

    static void runHook(final HookRunner hookRunner, final CustomerSignInResult customerSignInResult) {
        hookRunner.runEventHook(CustomerSignInResultLoadedHook.class, hook -> hook.onCustomerSignInResultLoaded(customerSignInResult));
    }
}
