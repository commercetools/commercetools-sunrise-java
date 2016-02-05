package ctpclient;

import com.google.inject.Provider;
import io.sphere.sdk.client.*;
import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.play.metrics.MetricAction;
import io.sphere.sdk.play.metrics.MetricHttpClient;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;

import static java.util.Objects.requireNonNull;

class SphereClientProvider implements Provider<SphereClient> {
    public static final String CONFIG_PROJECT_KEY = "ctp.projectKey";
    public static final String CONFIG_CLIENT_ID = "ctp.clientId";
    public static final String CONFIG_CLIENT_SECRET = "ctp.clientSecret";
    private final Configuration configuration;

    @Inject
    public SphereClientProvider(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SphereClient get() {
        final SphereClientConfig config = getClientConfig();
        final boolean metricsEnabled = configuration.getBoolean(MetricAction.CONFIG_METRICS_ENABLED);
        return metricsEnabled ? createClientWithMetrics(config) : createClient(config);
    }

    private SphereClient createClient(final SphereClientConfig clientConfig) {
        Logger.debug("Provide SphereClient");
        return SphereClientFactory.of().createClient(clientConfig);
    }

    private SphereClient createClientWithMetrics(final SphereClientConfig clientConfig) {
        final HttpClient underlyingHttpClient = SphereAsyncHttpClientFactory.create();
        final MetricHttpClient httpClient = MetricHttpClient.of(underlyingHttpClient);
        final SphereAccessTokenSupplier tokenSupplier = SphereAccessTokenSupplierFactory.of(() -> httpClient)
                .createSupplierOfAutoRefresh(clientConfig);
        Logger.debug("Provide SphereClient with metrics");
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
