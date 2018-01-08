package com.commercetools.sunrise.core.controllers;

import io.sphere.sdk.client.NotFoundException;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Resource;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;

@FunctionalInterface
public interface ResourceUpdater<T extends Resource<T>> {

    CompletionStage<Optional<T>> apply(List<UpdateAction<T>> updateActions);

    default CompletionStage<Optional<T>> apply(final UpdateAction<T> updateAction) {
        return apply(singletonList(updateAction));
    }

    default CompletionStage<T> force(List<UpdateAction<T>> updateActions) {
        return apply(updateActions).thenApply(resource -> resource.orElseThrow(NotFoundException::new));
    }

    default CompletionStage<T> force(UpdateAction<T> updateAction) {
        return force(singletonList(updateAction));
    }
}
