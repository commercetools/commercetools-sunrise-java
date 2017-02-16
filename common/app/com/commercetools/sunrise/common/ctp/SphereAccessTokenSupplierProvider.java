package com.commercetools.sunrise.common.ctp;

import com.google.inject.Provider;
import io.sphere.sdk.client.SphereAccessTokenSupplier;
import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class SphereAccessTokenSupplierProvider implements Provider<SphereAccessTokenSupplier> {

    private final ApplicationLifecycle applicationLifecycle;
    private final SphereClientConfig sphereClientConfig;
    private final HttpClient httpClient;

    @Inject
    public SphereAccessTokenSupplierProvider(final ApplicationLifecycle applicationLifecycle, final SphereClientConfig sphereClientConfig, final HttpClient httpClient) {
        this.applicationLifecycle = applicationLifecycle;
        this.sphereClientConfig = sphereClientConfig;
        this.httpClient = httpClient;
    }

    @Override
    public SphereAccessTokenSupplier get() {
        final SphereAccessTokenSupplier sphereAccessTokenSupplier =
                SphereAccessTokenSupplier.ofAutoRefresh(sphereClientConfig, httpClient, false);
        applicationLifecycle.addStopHook(() -> {
            sphereAccessTokenSupplier.close();
            return completedFuture(null);
        });
        return sphereAccessTokenSupplier;
    }
}
