package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.ResourceSearch;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractSearchExecutor<T, S extends ResourceSearch<T>> extends AbstractSphereRequestExecutor implements ResourceFetcher<T> {

    protected AbstractSearchExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<PagedSearchResult<T>> executeRequest(final S baseRequest) {
        final S request = runSearchHook(baseRequest);
        return getSphereClient().execute(request)
                .thenApplyAsync(result -> {
                    runPagedSearchResultLoadedHook(result);
                    return result;
                }, HttpExecution.defaultContext());
    }

    protected abstract S runSearchHook(S baseRequest);

    protected abstract CompletionStage<?> runPagedSearchResultLoadedHook(PagedSearchResult<T> result);
}
