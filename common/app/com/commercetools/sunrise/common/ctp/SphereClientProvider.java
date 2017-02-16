package com.commercetools.sunrise.common.ctp;

import com.google.inject.Provider;
import io.sphere.sdk.client.SphereAccessTokenSupplier;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class SphereClientProvider implements Provider<SphereClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SphereClientProvider.class);

    private final ApplicationLifecycle applicationLifecycle;
    private final SphereClientConfig sphereClientConfig;
    private final HttpClient httpClient;
    private final SphereAccessTokenSupplier sphereAccessTokenSupplier;

    @Inject
    public SphereClientProvider(final ApplicationLifecycle applicationLifecycle, final SphereClientConfig sphereClientConfig,
                                final HttpClient httpClient, final SphereAccessTokenSupplier sphereAccessTokenSupplier) {
        this.applicationLifecycle = applicationLifecycle;
        this.sphereClientConfig = sphereClientConfig;
        this.httpClient = httpClient;
        this.sphereAccessTokenSupplier = sphereAccessTokenSupplier;
    }

    @Override
    public SphereClient get() {
        final SphereClient sphereClient = SphereClient.of(sphereClientConfig, httpClient, sphereAccessTokenSupplier);
        LOGGER.debug("Initialize SphereClient");
        applicationLifecycle.addStopHook(() -> {
            sphereClient.close();
            return completedFuture(null);
        });
        return sphereClient;
    }
}
