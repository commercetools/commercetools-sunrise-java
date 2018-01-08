package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.sessions.ResourceInCache;
import io.sphere.sdk.client.ConcurrentModificationException;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.commands.UpdateCommand;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import io.sphere.sdk.models.Resource;
import play.libs.concurrent.HttpExecution;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.CompletableFutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.CompletableFutureUtils.recoverWith;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractResourceUpdater<T extends Resource<T>, C extends UpdateCommand<T> & ExpansionPathContainer<T>> extends AbstractSphereRequestExecutor implements ResourceUpdater<T> {

    private final ResourceInCache<T> resourceInCache;

    protected AbstractResourceUpdater(final SphereClient sphereClient, final HookRunner hookRunner, final ResourceInCache<T> resourceInCache) {
        super(sphereClient, hookRunner);
        this.resourceInCache = resourceInCache;
    }

    @Override
    public CompletionStage<Optional<T>> apply(final List<UpdateAction<T>> updateActions) {
        return recoverWith(executeRequest(updateActions, resourceInCache.get()), throwable -> {
            final Throwable causeThrowable = throwable.getCause();
            if (causeThrowable instanceof ConcurrentModificationException) {
                resourceInCache.remove();
                return executeRequest(updateActions, resourceInCache.get());
            }
            return exceptionallyCompletedFuture(throwable);
        }, HttpExecution.defaultContext());
    }

    private CompletionStage<Optional<T>> executeRequest(final List<UpdateAction<T>> updateActions, final CompletionStage<Optional<T>> resourceStage) {
        return resourceStage.thenComposeAsync(resourceOpt -> resourceOpt
                .map(resource -> executeRequest(resource, buildUpdateCommand(updateActions, resource))
                        .thenApply(Optional::of))
                .orElseGet(() -> completedFuture(Optional.empty())), HttpExecution.defaultContext());
    }

    protected final CompletionStage<T> executeRequest(final T resource, final C baseCommand) {
        final C command = runUpdateCommandHook(getHookRunner(), baseCommand);
        if (!command.getUpdateActions().isEmpty()) {
            return getSphereClient().execute(command)
                    .thenComposeAsync(updatedResource -> runActionHook(getHookRunner(), updatedResource, command)
                            .thenApplyAsync(finalUpdatedResource -> {
                                runUpdatedHook(getHookRunner(), finalUpdatedResource);
                                return finalUpdatedResource;
                            }, HttpExecution.defaultContext()),
                    HttpExecution.defaultContext());
        } else {
            return completedFuture(resource);
        }
    }

    protected abstract C buildUpdateCommand(final List<UpdateAction<T>> updateActions, final T resource);

    protected abstract C runUpdateCommandHook(HookRunner hookRunner, C baseCommand);

    protected abstract CompletionStage<T> runActionHook(HookRunner hookRunner, T resource, final ExpansionPathContainer<T> expansionPathContainer);

    protected abstract CompletionStage<?> runUpdatedHook(HookRunner hookRunner, T resource);
}
