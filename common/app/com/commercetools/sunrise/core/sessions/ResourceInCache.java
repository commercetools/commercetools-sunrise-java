package com.commercetools.sunrise.core.sessions;

import io.sphere.sdk.models.Resource;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

public interface ResourceInCache<T extends Resource<T>> extends StoringOperations<T>, Supplier<CompletionStage<Optional<T>>> {

    @Override
    CompletionStage<Optional<T>> get();

    @Override
    void store(@Nullable final T resource);

    @Override
    void remove();
}
