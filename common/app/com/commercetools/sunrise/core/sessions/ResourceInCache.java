package com.commercetools.sunrise.core.sessions;

import com.commercetools.sunrise.core.NotFoundResourceException;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface ResourceInCache<T> extends StoringOperations<T> {

    CompletionStage<Optional<T>> get();

    default CompletionStage<T> require() {
        return get().thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }

    void purge();
}
