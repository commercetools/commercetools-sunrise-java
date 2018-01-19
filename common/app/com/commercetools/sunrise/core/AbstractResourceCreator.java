package com.commercetools.sunrise.core;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.CreateCommand;
import io.sphere.sdk.expansion.ExpansionPathContainer;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class AbstractResourceCreator<T, D, C extends CreateCommand<T> & ExpansionPathContainer<T>> extends AbstractSphereRequestExecutor implements ResourceCreator<T, D> {

    protected AbstractResourceCreator(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public final CompletionStage<T> get(final D draft) {
        return executeRequest(buildRequest(draft));
    }

    protected CompletionStage<T> executeRequest(final C baseCommand) {
        return runHook(baseCommand, command -> getSphereClient().execute(command));
    }

    protected abstract C buildRequest(D draft);

    protected abstract CompletionStage<T> runHook(C command, Function<C, CompletionStage<T>> execution);
}
