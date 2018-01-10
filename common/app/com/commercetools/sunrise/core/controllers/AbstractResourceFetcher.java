package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.queries.PagedResult;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractResourceFetcher<T, R extends SphereRequest<P>, P extends PagedResult<T>> extends AbstractSphereRequestExecutor implements ResourceFetcher<P, R> {

    protected AbstractResourceFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<P> get(final R request) {
        return executeRequest(request);
    }

    protected final CompletionStage<P> executeRequest(final R baseRequest) {
        final R request = runRequestHook(baseRequest);
        return getSphereClient().execute(request)
                .thenApplyAsync(result -> {
                    runPagedResultLoadedHook(result);
                    return result;
                }, HttpExecution.defaultContext());
    }

    protected abstract R runRequestHook(R baseRequest);

    protected abstract CompletionStage<?> runPagedResultLoadedHook(P pagedResult);
}
