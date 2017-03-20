package com.commercetools.sunrise.framework.hooks.application;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import play.mvc.Http;

import java.util.concurrent.CompletionStage;

/**
 * The first hook to be run, right after the request has been received from the customer.
 *
 * Enabled using {@code @EnableHooks} annotation.
 */
public interface HttpRequestStartedHook extends ApplicationHook {

    CompletionStage<?> onHttpRequestStarted(final Http.Context httpContext);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Http.Context httpContext) {
        return hookRunner.runEventHook(HttpRequestStartedHook.class, hook -> hook.onHttpRequestStarted(httpContext));
    }
}
