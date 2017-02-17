package com.commercetools.sunrise.hooks;

import com.commercetools.sunrise.hooks.events.RequestStartedHook;
import com.google.inject.Injector;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Action that runs the {@link com.commercetools.sunrise.hooks.requests.RequestHook} for the request.
 */
public final class RequestHookAction extends Action.Simple {

    private final Injector injector;

    @Inject
    public RequestHookAction(final Injector injector) {
        this.injector = injector;
    }

    @Override
    public CompletionStage<Result> call(final Http.Context ctx) {
        System.out.println("running hook");
        final HookRunner hookRunner = injector.getInstance(HookRunner.class);
        RequestStartedHook.runHook(hookRunner, ctx);
        return delegate.call(ctx);
    }
}
