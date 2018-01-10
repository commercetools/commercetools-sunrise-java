package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.queries.PagedResult;
import play.libs.concurrent.HttpExecution;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class AbstractSingleResourceFetcher<T, R extends SphereRequest<P>, P extends PagedResult<T>>  extends AbstractSphereRequestExecutor implements SingleResourceFetcher<T, R> {

    protected AbstractSingleResourceFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Optional<T>> get(final R request) {
        return executeRequest(request);
    }

    protected final CompletionStage<Optional<T>> executeRequest(final R baseRequest) {
        return executeRequest(baseRequest, this::selectResource);
    }

    protected CompletionStage<Optional<T>> executeRequest(final R baseRequest, final Function<P, Optional<T>> resourceSelector) {
        final R request = runRequestHook(baseRequest);
        return getSphereClient().execute(request)
                .thenApply(resourceSelector)
                .thenApplyAsync(resourceOpt -> {
                    resourceOpt.ifPresent(this::runResourceLoadedHook);
                    return resourceOpt;
                }, HttpExecution.defaultContext());
    }

    protected Optional<T> selectResource(final P pagedResult) {
        return pagedResult.head();
    }

    protected abstract R runRequestHook(R baseRequest);

    protected abstract CompletionStage<?> runResourceLoadedHook(T resource);
}
