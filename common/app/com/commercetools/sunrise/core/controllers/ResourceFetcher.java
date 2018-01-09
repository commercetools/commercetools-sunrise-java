package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.NotFoundResourceException;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.queries.PagedResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface ResourceFetcher<T, Q extends SphereRequest<P>, P extends PagedResult<T>> {

    CompletionStage<Optional<T>> get(final Q request);

    default CompletionStage<T> require(final Q request) {
        return get(request).thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
