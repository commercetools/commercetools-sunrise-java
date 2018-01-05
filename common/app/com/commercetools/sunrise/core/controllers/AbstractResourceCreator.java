package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.Command;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractResourceCreator<T, C extends Command<T>> extends AbstractSphereRequestExecutor implements ResourceCreator<T> {

    protected AbstractResourceCreator(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<T> executeRequest(final C baseCommand) {
        final C command = runCreateCommandHook(baseCommand);
        return getSphereClient().execute(command)
                .thenApplyAsync(updatedResource -> {
                    runCreatedHook(updatedResource);
                    return updatedResource;
                }, HttpExecution.defaultContext());
    }

    protected abstract C runCreateCommandHook(C baseCommand);

    protected abstract CompletionStage<?> runCreatedHook(T resource);
}
