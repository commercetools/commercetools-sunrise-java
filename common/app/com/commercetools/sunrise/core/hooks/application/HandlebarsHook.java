package com.commercetools.sunrise.core.hooks.application;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ReducerHook;
import com.github.jknack.handlebars.Handlebars;

import java.util.concurrent.CompletionStage;

public interface HandlebarsHook extends ReducerHook {

    CompletionStage<Handlebars> onHandlebarsCreated(final Handlebars handlebars);

    static CompletionStage<Handlebars> runHook(final HookRunner hookRunner, final Handlebars handlebars) {
        return hookRunner.run(HandlebarsHook.class, handlebars, (r, h) -> h.onHandlebarsCreated(r));
    }
}