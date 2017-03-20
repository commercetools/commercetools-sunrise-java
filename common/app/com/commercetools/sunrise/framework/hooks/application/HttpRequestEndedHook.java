package com.commercetools.sunrise.framework.hooks.application;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import play.mvc.Result;

/**
 * The last hook to be run, right before the response is going to be sent to the customer.
 *
 * Enabled using {@code @EnableHooks} annotation.
 */
public interface HttpRequestEndedHook extends ApplicationHook {

    Result onHttpRequestEnded(final Result result);

    static Result runHook(final HookRunner hookRunner, final Result result) {
        return hookRunner.runUnaryOperatorHook(HttpRequestEndedHook.class, HttpRequestEndedHook::onHttpRequestEnded, result);
    }
}
