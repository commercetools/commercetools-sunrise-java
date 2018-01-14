package com.commercetools.sunrise.core.hooks.application;

import com.commercetools.sunrise.core.hooks.HookRunner;
import play.mvc.Http;

/**
 * The first hook to be run, right after the request has been received from the customer.
 *
 * Enabled using {@code @EnableHooks} annotation.
 */
public interface HttpRequestStartedHook extends ApplicationHook {

    void onHttpRequestStarted(final Http.Context httpContext);

    static void runHook(final HookRunner hookRunner, final Http.Context httpContext) {
        hookRunner.runEventHook(HttpRequestStartedHook.class, hook -> hook.onHttpRequestStarted(httpContext));
    }
}
