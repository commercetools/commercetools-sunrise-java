package com.commercetools.sunrise.framework.hooks.ctpevents;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.customers.CustomerSignInResult;

import java.util.concurrent.CompletionStage;

public interface CustomerSignInResultLoadedHook extends CtpEventHook {

    CompletionStage<?> onCustomerSignInResultLoaded(final CustomerSignInResult customerSignInResult);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final CustomerSignInResult customerSignInResult) {
        return hookRunner.runEventHook(CustomerSignInResultLoadedHook.class, hook -> hook.onCustomerSignInResultLoaded(customerSignInResult));
    }
}
