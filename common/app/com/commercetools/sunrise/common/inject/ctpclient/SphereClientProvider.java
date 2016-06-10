package com.commercetools.sunrise.common.inject.ctpclient;

import com.google.inject.Provider;
import io.sphere.sdk.client.*;
import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.play.metrics.MetricAction;
import io.sphere.sdk.play.metrics.MetricHttpClient;
import play.Configuration;
import play.Logger;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.CompletableFuture.completedFuture;

class SphereClientProvider implements Provider<SphereClient> {

    private static final String CONFIG_PROJECT_KEY = "ctp.projectKey";
    private static final String CONFIG_CLIENT_ID = "ctp.clientId";
    private static final String CONFIG_CLIENT_SECRET = "ctp.clientSecret";

    @Inject
    private ApplicationLifecycle applicationLifecycle;
    @Inject
    private Configuration configuration;

    @Override
    public SphereClient get() {
        final SphereClientConfig config = getClientConfig();
        final SphereClient sphereClient = createClient(config);
        applicationLifecycle.addStopHook(() -> {
            sphereClient.close();
            return completedFuture(null);
        });
        return sphereClient;
    }

    private SphereClient createClient(final SphereClientConfig config) {
        final boolean metricsEnabled = configuration.getBoolean(MetricAction.CONFIG_METRICS_ENABLED);
        return metricsEnabled ? createClientWithMetrics(config) : createRegularClient(config);
    }

    private SphereClient createRegularClient(final SphereClientConfig clientConfig) {
        Logger.info("Provide SphereClient");
        return SphereClientFactory.of().createClient(clientConfig);
    }

    private SphereClient createClientWithMetrics(final SphereClientConfig clientConfig) {
        final HttpClient underlyingHttpClient = SphereAsyncHttpClientFactory.create();
        final MetricHttpClient httpClient = MetricHttpClient.of(underlyingHttpClient);
        final SphereAccessTokenSupplier tokenSupplier = SphereAccessTokenSupplier.ofAutoRefresh(clientConfig, httpClient, false);
        Logger.info("Provide SphereClient with metrics");
        return SphereClient.of(clientConfig, httpClient, tokenSupplier);
    }

    private SphereClientConfig getClientConfig() {
        final String project = requireNonNull(configuration.getString(CONFIG_PROJECT_KEY),
                "No CTP project key defined in configuration '" + CONFIG_PROJECT_KEY + "'");
        final String clientId = requireNonNull(configuration.getString(CONFIG_CLIENT_ID),
                "No CTP client ID defined in configuration '" + CONFIG_CLIENT_ID + "'");
        final String clientSecret = requireNonNull(configuration.getString(CONFIG_CLIENT_SECRET),
                "No CTP client secret defined in configuration '" + CONFIG_CLIENT_SECRET + "'");
        return SphereClientConfig.of(project, clientId, clientSecret);
    }
}
