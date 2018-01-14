package com.commercetools.sunrise.core;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.CreateCommand;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractResourceCreator<T, D, C extends CreateCommand<T> & ExpansionPathContainer<T>> extends AbstractSphereRequestExecutor implements ResourceCreator<T, D> {

    protected AbstractResourceCreator(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public final CompletionStage<T> get(final D draft) {
        return executeRequest(buildRequest(draft));
    }

    protected CompletionStage<T> executeRequest(final C baseCommand) {
        return runCreateCommandHook(getHookRunner(), baseCommand)
                .thenCompose(command -> getSphereClient().execute(command)
                        .thenComposeAsync(result -> applyHooks(command, result), HttpExecution.defaultContext()));
    }

    private CompletionStage<T> applyHooks(final C command, final T resource) {
        final CompletionStage<T> finalResourceStage = runActionHook(getHookRunner(), resource, command);
        finalResourceStage.thenAcceptAsync(finalResource -> runCreatedHook(getHookRunner(), finalResource), HttpExecution.defaultContext());
        return finalResourceStage;
    }

    protected abstract C buildRequest(D draft);

    protected abstract CompletionStage<C> runCreateCommandHook(HookRunner hookRunner, C baseCommand);

    protected abstract void runCreatedHook(HookRunner hookRunner, T resource);

    protected abstract CompletionStage<T> runActionHook(HookRunner hookRunner, T resource, ExpansionPathContainer<T> expansionPathContainer);
}
