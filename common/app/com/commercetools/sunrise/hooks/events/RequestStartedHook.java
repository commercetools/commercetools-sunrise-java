package com.commercetools.sunrise.hooks.events;

import com.commercetools.sunrise.hooks.HookRunner;
import play.mvc.Http;

import java.util.concurrent.CompletionStage;

public interface RequestStartedHook extends EventHook {

    CompletionStage<?> onRequestStarted(final Http.Context httpContext);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Http.Context httpContext) {
        return hookRunner.runEventHook(RequestStartedHook.class, hook -> hook.onRequestStarted(httpContext));
    }
}
