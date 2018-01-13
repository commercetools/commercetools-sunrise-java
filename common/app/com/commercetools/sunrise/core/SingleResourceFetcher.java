package com.commercetools.sunrise.core;

import com.commercetools.sunrise.core.NotFoundResourceException;
import io.sphere.sdk.client.SphereRequest;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface SingleResourceFetcher<T, R extends SphereRequest<?>> {

    CompletionStage<Optional<T>> get(final R request);

    default CompletionStage<T> require(final R request) {
        return get(request).thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
