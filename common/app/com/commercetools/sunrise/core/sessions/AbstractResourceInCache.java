package com.commercetools.sunrise.core.sessions;

import play.cache.CacheApi;
import play.libs.concurrent.HttpExecution;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractResourceInCache<T> implements ResourceInCache<T> {

    private final CacheApi cacheApi;

    protected AbstractResourceInCache(final CacheApi cacheApi) {
        this.cacheApi = cacheApi;
    }

    @Override
    public CompletionStage<T> get() {
        // TODO use expiration date!
        final String cacheKey = cacheKey();
        final T nullableResource = cacheApi.get(cacheKey);
        return Optional.ofNullable(nullableResource)
                .map(cart -> (CompletionStage<T>) completedFuture(cart))
                .orElseGet(() -> fetchAndStoreResource(cacheKey));
    }

    @Override
    public void store(@Nullable final T resource) {
        if (resource != null) {
            cacheApi.set(cacheKey(), resource);
        } else {
            remove();
        }
    }

    @Override
    public void remove() {
        cacheApi.remove(cacheKey());
    }

    @Override
    public void purge() {
        remove();
    }

    private CompletionStage<T> fetchAndStoreResource(final String cacheKey) {
        final CompletionStage<T> resourceStage = fetchResource();
        resourceStage.thenAcceptAsync(resource -> cacheApi.set(cacheKey, resource), HttpExecution.defaultContext());
        return resourceStage;
    }

    protected abstract CompletionStage<T> fetchResource();

    protected abstract String cacheKey();
}
