package com.commercetools.sunrise.ctp;

import com.google.inject.Provider;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.client.SphereClientFactory;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class SphereClientProvider implements Provider<SphereClient> {

    private final ApplicationLifecycle applicationLifecycle;
    private final SphereClientConfig sphereClientConfig;

    @Inject
    public SphereClientProvider(final ApplicationLifecycle applicationLifecycle, final SphereClientConfig sphereClientConfig) {
        this.applicationLifecycle = applicationLifecycle;
        this.sphereClientConfig = sphereClientConfig;
    }

    @Override
    public SphereClient get() {
        final SphereClient sphereClient = SphereClientFactory.of().createClient(sphereClientConfig);
        applicationLifecycle.addStopHook(() -> {
            sphereClient.close();
            return completedFuture(null);
        });
        return sphereClient;
    }
}
