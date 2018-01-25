package com.commercetools.sunrise.core;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.sessions.UserResourceInCache;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.commands.UpdateCommandDsl;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import io.sphere.sdk.models.Resource;
import play.libs.concurrent.HttpExecution;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractUserResourceUpdater<T extends Resource<T>, R extends UpdateCommandDsl<T, R> & ExpansionPathContainer<T>> extends AbstractHookRunner<T, R> implements ResourceUpdater<T, R> {

    private final SphereClient sphereClient;
    private final UserResourceInCache<T> resourceInCache;

    protected AbstractUserResourceUpdater(final HookRunner hookRunner, final SphereClient sphereClient,
                                          final UserResourceInCache<T> resourceInCache) {
        super(hookRunner);
        this.sphereClient = sphereClient;
        this.resourceInCache = resourceInCache;
    }

    protected final SphereClient sphereClient() {
        return sphereClient;
    }

    protected final UserResourceInCache<T> resourceInCache() {
        return resourceInCache;
    }

    @Override
    public CompletionStage<Optional<T>> apply(final List<? extends UpdateAction<T>> updateActions) {
        return resourceInCache.get()
                .thenComposeAsync(resourceOpt -> resourceOpt
                        .map(resource -> buildRequest(resource, updateActions))
                        .map(request -> executeRequest(request).thenApply(Optional::of))
                        .orElseGet(() -> completedFuture(Optional.empty())), HttpExecution.defaultContext());
    }

    protected final CompletionStage<T> executeRequest(final R request) {
        return runHook(request, finalRequest -> {
            final CompletionStage<T> resourceStage = sphereClient.execute(finalRequest);
            resourceStage.thenAcceptAsync(resourceInCache::store, HttpExecution.defaultContext());
            return resourceStage;
        });
    }

    protected abstract R buildRequest(final T resource, final List<? extends UpdateAction<T>> updateActions);
}
