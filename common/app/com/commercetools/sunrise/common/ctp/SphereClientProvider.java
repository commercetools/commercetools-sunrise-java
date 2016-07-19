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
    private static final Logger logger = LoggerFactory.getLogger(SphereClientProvider.class);

    @Inject
    private ApplicationLifecycle applicationLifecycle;
    @Inject
    private HttpClient httpClient;
    @Inject
    private SphereAccessTokenSupplier sphereAccessTokenSupplier;
    @Inject
    private SphereClientConfig sphereClientConfig;

    @Override
    public SphereClient get() {
        SphereClient sphereClient = SphereClient.of(sphereClientConfig, httpClient, sphereAccessTokenSupplier);
        logger.info("Provide SphereClient");
        applicationLifecycle.addStopHook(() -> {
            sphereClient.close();
            return completedFuture(null);
        });
        return sphereClient;
    }

}
