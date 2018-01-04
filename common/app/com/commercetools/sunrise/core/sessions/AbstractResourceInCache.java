package com.commercetools.sunrise.core.sessions;

import io.sphere.sdk.models.Resource;
import play.cache.CacheApi;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractResourceInCache<T extends Resource<T>> implements ResourceInCache<T> {

    private final ResourceInSession<T> resourceInSession;
    private final CacheApi cacheApi;

    protected AbstractResourceInCache(final ResourceInSession<T> resourceInSession, final CacheApi cacheApi) {
        this.resourceInSession = resourceInSession;
        this.cacheApi = cacheApi;
    }

    @Override
    public CompletionStage<Optional<T>> get() {
        return resourceInSession.findId()
                .map(this::generateCacheKey)
                .map(this::findInCacheOrFetch)
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    private CompletionStage<Optional<T>> findInCacheOrFetch(final String cacheKey) {
        final T nullableResource = cacheApi.get(cacheKey);
        return Optional.ofNullable(nullableResource)
                .filter(this::isUpToDate)
                .map(Optional::of)
                .map(cart -> (CompletionStage<Optional<T>>) completedFuture(cart))
                .orElseGet(this::fetchResource);
    }

    protected boolean isUpToDate(final T resource) {
        return resourceInSession.findVersion()
                .map(version -> resource.getVersion().equals(version))
                .orElse(false);
    }

    @Override
    public void store(@Nullable final T resource) {
        if (resource != null) {
            final String cacheKey = generateCacheKey(resource.getId());
            cacheApi.set(cacheKey, resource);
        } else {
            remove();
        }
    }

    @Override
    public void remove() {
        resourceInSession.findId().ifPresent(id -> {
            final String cacheKey = generateCacheKey(id);
            cacheApi.remove(cacheKey);
        });
    }

    protected abstract String generateCacheKey(final String resourceId);

    protected abstract CompletionStage<Optional<T>> fetchResource();
}
