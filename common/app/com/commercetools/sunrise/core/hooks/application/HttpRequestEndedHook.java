package com.commercetools.sunrise.core.hooks.application;

import com.commercetools.sunrise.core.hooks.HookRunner;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * The last hook to be run, right before the response is going to be sent to the customer.
 *
 * Enabled using {@code @EnableHooks} annotation.
 */
public interface HttpRequestEndedHook extends ApplicationHook {

    CompletionStage<Result> onHttpRequestEnded(final Result result);

    static CompletionStage<Result> runHook(final HookRunner hookRunner, final Result result) {
        return hookRunner.runActionHook(HttpRequestEndedHook.class, HttpRequestEndedHook::onHttpRequestEnded, result);
    }
}
