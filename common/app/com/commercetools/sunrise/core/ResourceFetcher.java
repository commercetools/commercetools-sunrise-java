package com.commercetools.sunrise.core;

import io.sphere.sdk.client.SphereRequest;

import java.util.concurrent.CompletionStage;

public interface ResourceFetcher<P, R extends SphereRequest<?>> {

    CompletionStage<P> get(final R request);
}
