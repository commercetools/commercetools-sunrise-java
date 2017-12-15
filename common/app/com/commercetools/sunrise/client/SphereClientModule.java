package com.commercetools.sunrise.client;

import com.commercetools.sunrise.core.controllers.metrics.SimpleMetricsSphereClientProvider;
import com.google.inject.AbstractModule;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientConfig;

import javax.inject.Singleton;

/**
 * Module that allows to inject a {@link SphereClient} and its configuration.
 */
public final class SphereClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SphereClientConfig.class)
                .toProvider(SphereClientConfigProvider.class)
                .in(Singleton.class);

        bind(SphereClient.class)
                .toProvider(SimpleMetricsSphereClientProvider.class)
                .in(Singleton.class);
    }
}
