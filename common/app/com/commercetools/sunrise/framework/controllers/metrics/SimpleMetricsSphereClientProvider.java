package com.commercetools.sunrise.framework.controllers.metrics;

import com.google.inject.Provider;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.client.metrics.SimpleMetricsSphereClient;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class SimpleMetricsSphereClientProvider implements Provider<SimpleMetricsSphereClient> {

    private final ApplicationLifecycle applicationLifecycle;
    private final SphereClientConfig sphereClientConfig;

    @Inject
    public SimpleMetricsSphereClientProvider(final ApplicationLifecycle applicationLifecycle, final SphereClientConfig sphereClientConfig) {
        this.applicationLifecycle = applicationLifecycle;
        this.sphereClientConfig = sphereClientConfig;
    }

    @Override
    public SimpleMetricsSphereClient get() {
        final SphereClient sphereClient = SphereClientFactory.of().createClient(sphereClientConfig);
        final SimpleMetricsSphereClient metricsSphereClient = SimpleMetricsSphereClient.of(sphereClient);
        applicationLifecycle.addStopHook(() -> {
            metricsSphereClient.close();
            return completedFuture(null);
        });
        return metricsSphereClient;
    }
}
