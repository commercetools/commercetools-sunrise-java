package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.queries.ResourceQuery;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractQueryExecutor<T, Q extends ResourceQuery<T>> extends AbstractSphereRequestExecutor implements ResourceFetcher<T> {

    protected AbstractQueryExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<PagedQueryResult<T>> executeRequest(final Q baseRequest) {
        final Q request = runQueryHook(baseRequest);
        return getSphereClient().execute(request)
                .thenApplyAsync(result -> {
                    runPagedQueryResultLoadedHook(result);
                    return result;
                }, HttpExecution.defaultContext());
    }

    protected abstract Q runQueryHook(Q baseRequest);

    protected abstract CompletionStage<?> runPagedQueryResultLoadedHook(PagedQueryResult<T> result);
}
