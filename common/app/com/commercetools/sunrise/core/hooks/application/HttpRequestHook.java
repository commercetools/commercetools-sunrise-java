package com.commercetools.sunrise.core.hooks.application;

import com.commercetools.sunrise.core.hooks.FilterHook;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * The first hook to be run, right after the request has been received from the customer.
 *
 * Enabled using {@code @EnableHooks} annotation.
 */
public interface HttpRequestHook extends FilterHook {

    CompletionStage<Result> on(Http.Context ctx, Function<Http.Context, CompletionStage<Result>> nextComponent);
}
