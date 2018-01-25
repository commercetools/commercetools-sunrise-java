package com.commercetools.sunrise.core;

import com.commercetools.sunrise.core.hooks.HookRunner;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class AbstractHookRunner<T, R> {

    private final HookRunner hookRunner;

    protected AbstractHookRunner(final HookRunner hookRunner) {
        this.hookRunner = hookRunner;
    }

    protected final HookRunner hookRunner() {
        return hookRunner;
    }

    protected abstract CompletionStage<T> runHook(R request, Function<R, CompletionStage<T>> execution);
}
