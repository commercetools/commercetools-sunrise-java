package com.commercetools.sunrise.core.sessions;

import com.commercetools.sunrise.core.NotFoundResourceException;
import io.sphere.sdk.models.Resource;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface ResourceInCache<T extends Resource<T>> extends StoringOperations<T> {

    CompletionStage<Optional<T>> get();

    @Override
    void store(@Nullable final T resource);

    @Override
    void remove();

    default CompletionStage<T> require() {
        return get().thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
