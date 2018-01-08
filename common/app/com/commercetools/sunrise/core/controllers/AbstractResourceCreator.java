package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.CreateCommand;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractResourceCreator<T, C extends CreateCommand<T> & ExpansionPathContainer<T>> extends AbstractSphereRequestExecutor implements ResourceCreator<T> {

    protected AbstractResourceCreator(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<T> executeRequest(final C baseCommand) {
        final C command = runCreateCommandHook(getHookRunner(), baseCommand);
        return getSphereClient().execute(command)
                .thenComposeAsync(result -> runActionHook(getHookRunner(), result, command)
                        .thenApplyAsync(updatedResource -> {
                            runCreatedHook(getHookRunner(), updatedResource);
                            return updatedResource;
                        }, HttpExecution.defaultContext()),
                        HttpExecution.defaultContext());
    }

    protected abstract C runCreateCommandHook(HookRunner hookRunner, C baseCommand);

    protected abstract CompletionStage<?> runCreatedHook(HookRunner hookRunner, T resource);

    protected abstract CompletionStage<T> runActionHook(HookRunner hookRunner, T resource, ExpansionPathContainer<T> expansionPathContainer);
}
