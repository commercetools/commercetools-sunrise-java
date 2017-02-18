package com.commercetools.sunrise.hooks;

import com.commercetools.sunrise.hooks.events.RequestStartedHook;
import com.google.inject.Injector;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Action that runs the {@link com.commercetools.sunrise.hooks.events.RequestStartedHook} for the request.
 */
public final class RequestStartedHookRunnerAction extends Action.Simple {

    private final Injector injector;

    @Inject
    public RequestStartedHookRunnerAction(final Injector injector) {
        this.injector = injector;
    }

    @Override
    public CompletionStage<Result> call(final Http.Context ctx) {
        // On creation of this action there isn't any HTTP context, necessary to initialize the HookRunner
        final HookRunner hookRunner = injector.getInstance(HookRunner.class);
        RequestStartedHook.runHook(hookRunner, ctx);
        return delegate.call(ctx);
    }
}
