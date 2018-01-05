package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.Command;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import play.libs.concurrent.HttpExecution;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

public abstract class AbstractResourceUpdater<T, C extends Command<T>> extends AbstractSphereRequestExecutor implements ResourceCreator<T> {

    protected AbstractResourceUpdater(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<T> executeRequest(final C baseCommand, @Nullable final ExpansionPathContainer<T> expansionPathContainer) {
        final C command = runCreateCommandHook(baseCommand);
        return getSphereClient().execute(command)
                .thenComposeAsync(resource -> runActionHook(resource, expansionPathContainer)
                                .thenApplyAsync(updatedResource -> {
                                    runCreatedHook(updatedResource);
                                    return updatedResource;
                                }, HttpExecution.defaultContext()),
                        HttpExecution.defaultContext());
    }

    protected abstract C runCreateCommandHook(C baseCommand);

    protected abstract CompletionStage<?> runCreatedHook(T resource);

    protected abstract CompletionStage<T> runActionHook(T resource, @Nullable final ExpansionPathContainer<T> expansionPathContainer);
}
