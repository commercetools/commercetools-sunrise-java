package com.commercetools.sunrise.common.controllers;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientDecorator;
import io.sphere.sdk.client.SphereRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletionStage;

final class HookedSphereClient extends SphereClientDecorator implements SphereClient {
    private static final Logger logger = LoggerFactory.getLogger(HookedSphereClient.class);
    private final SunriseFrameworkController sunriseFrameworkController;

    public HookedSphereClient(final SphereClient delegate, final SunriseFrameworkController sunriseFrameworkController) {
        super(delegate);
        this.sunriseFrameworkController = sunriseFrameworkController;
    }

    @Override
    public void close() {
        logger.warn("ignored attempt to close SphereClient within " + this);
    }

    @Override
    public <T> CompletionStage<T> execute(final SphereRequest<T> sphereRequest) {
        return super.execute(sphereRequest);
    }
}
