package com.commercetools.sunrise.core.hooks;

import com.commercetools.sunrise.core.hooks.application.HttpRequestHook;
import play.inject.Injector;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class HooksEnabler extends Action<EnableHooks> {

    private final Injector injector;

    @Inject
    HooksEnabler(final Injector injector) {
        this.injector = injector;
    }

    @Override
    public CompletionStage<Result> call(final Http.Context ctx) {
        // On creation of this action there isn't any HTTP context, necessary to initialize the HookRunner
        final HookContext hookRunner = injector.instanceOf(HookContext.class);
        return hookRunner.run(HttpRequestHook.class, ctx, c -> delegate.call(c), h -> h::on);
    }
}
