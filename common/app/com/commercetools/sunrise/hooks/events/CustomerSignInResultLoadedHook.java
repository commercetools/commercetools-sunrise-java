package com.commercetools.sunrise.hooks.events;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.customers.CustomerSignInResult;

import java.util.concurrent.CompletionStage;

public interface CustomerSignInResultLoadedHook extends EventHook {

    CompletionStage<?> onCustomerSignInResultLoaded(final CustomerSignInResult customerSignInResult);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final CustomerSignInResult customerSignInResult) {
        return hookRunner.runEventHook(CustomerSignInResultLoadedHook.class, hook -> hook.onCustomerSignInResultLoaded(customerSignInResult));
    }
}
