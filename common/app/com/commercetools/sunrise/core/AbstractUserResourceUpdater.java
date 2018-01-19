package com.commercetools.sunrise.core;

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

public abstract class AbstractUserResourceUpdater<T extends Resource<T>, C extends UpdateCommand<T> & ExpansionPathContainer<T>> extends AbstractSphereRequestExecutor implements ResourceUpdater<T> {

    private final ResourceInCache<T> resourceInCache;

    protected AbstractUserResourceUpdater(final SphereClient sphereClient, final HookRunner hookRunner, final ResourceInCache<T> resourceInCache) {
        super(sphereClient, hookRunner);
        this.resourceInCache = resourceInCache;
    }

    @Override
    public CompletionStage<Optional<T>> apply(final List<? extends UpdateAction<T>> updateActions) {
        return recoverWith(executeRequest(updateActions, resourceInCache.get()), throwable -> {
            final Throwable causeThrowable = throwable.getCause();
            if (causeThrowable instanceof ConcurrentModificationException) {
                return handleConcurrentModification(updateActions, (ConcurrentModificationException) causeThrowable);
            }
            return exceptionallyCompletedFuture(throwable);
        }, HttpExecution.defaultContext());
    }

    private CompletionStage<Optional<T>> executeRequest(final List<? extends UpdateAction<T>> updateActions, final CompletionStage<Optional<T>> resourceStage) {
        return resourceStage.thenComposeAsync(resourceOpt -> resourceOpt
                .map(resource -> executeRequest(resource, buildUpdateCommand(updateActions, resource))
                        .thenApply(Optional::of))
                .orElseGet(() -> completedFuture(Optional.empty())), HttpExecution.defaultContext());
    }

    protected final CompletionStage<T> executeRequest(final T resource, final C baseCommand) {
        return runRequestHook(baseCommand).thenCompose(command -> {
            if (command.getUpdateActions().isEmpty()) {
                return completedFuture(resource);
            } else {
                final CompletionStage<T> resourceStage = getSphereClient().execute(command)
                        .thenComposeAsync(updatedResource -> applyHooks(command, updatedResource), HttpExecution.defaultContext());
                resourceStage.thenAcceptAsync(resourceInCache::store);
                return resourceStage;
            }
        });
    }

    private CompletionStage<T> applyHooks(final C command, final T resource) {
        final CompletionStage<T> finalResourceStage = runResponseHook(resource, command);
        finalResourceStage.thenAcceptAsync(this::runUpdatedHook, HttpExecution.defaultContext());
        return finalResourceStage;
    }

    protected abstract C buildUpdateCommand(final List<? extends UpdateAction<T>> updateActions, final T resource);

    protected abstract CompletionStage<C> runRequestHook(C baseCommand);

    protected abstract CompletionStage<T> runResponseHook(T resource, final ExpansionPathContainer<T> expansionPathContainer);

    protected abstract void runUpdatedHook(T resource);

    protected CompletionStage<Optional<T>> handleConcurrentModification(final List<? extends UpdateAction<T>> updateActions,
                                                                        final ConcurrentModificationException exception) {
        resourceInCache.purge();
        return executeRequest(updateActions, resourceInCache.get());
    }
}
