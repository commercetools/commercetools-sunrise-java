package com.commercetools.sunrise.common.ctp;

import com.google.inject.Provider;
import io.sphere.sdk.client.SphereAccessTokenSupplier;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import javax.inject.Inject;

public final class MetricSphereClientProvider implements Provider<SphereClient> {

    private final HttpClient httpClient;
    private final SphereAccessTokenSupplier sphereAccessTokenSupplier;
    private final SphereClientConfig sphereClientConfig;
    private final Http.Context context;

    @Inject
    public MetricSphereClientProvider(final HttpClient httpClient, final SphereAccessTokenSupplier sphereAccessTokenSupplier,
                                      final SphereClientConfig sphereClientConfig, final Http.Context context) {
        this.httpClient = httpClient;
        this.sphereAccessTokenSupplier = sphereAccessTokenSupplier;
        this.sphereClientConfig = sphereClientConfig;
        this.context = context;
    }

    @Override
    public SphereClient get() {
        final MetricHttpClient metricHttpClient = MetricHttpClient.of(httpClient, context);
        return SphereClient.of(sphereClientConfig, metricHttpClient, sphereAccessTokenSupplier);
    }

}
