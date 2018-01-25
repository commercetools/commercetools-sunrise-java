package com.commercetools.sunrise.core;

import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Resource;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;

public interface ResourceUpdater<T extends Resource<T>, R> {

    CompletionStage<Optional<T>> apply(List<? extends UpdateAction<T>> updateActions);

    default CompletionStage<Optional<T>> apply(final UpdateAction<T> updateAction) {
        return apply(singletonList(updateAction));
    }

    default CompletionStage<T> force(final List<? extends UpdateAction<T>> updateActions) {
        return apply(updateActions).thenApply(resource -> resource.orElseThrow(NotFoundResourceException::new));
    }

    default CompletionStage<T> force(final UpdateAction<T> updateAction) {
        return force(singletonList(updateAction));
    }
}
