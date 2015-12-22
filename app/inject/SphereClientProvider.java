package inject;

import com.google.inject.Provider;
import io.sphere.sdk.client.*;
import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.play.metrics.MetricHttpClient;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;

import static java.util.Objects.requireNonNull;

class SphereClientProvider implements Provider<SphereClient> {
    private final Configuration configuration;

    @Inject
    public SphereClientProvider(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SphereClient get() {
        Logger.debug("Provide SphereClient");
        final SphereClientConfig config = getSphereClientConfig();
        return createSphereClient(config);
    }

    private SphereClient createSphereClient(final SphereClientConfig config) {
        final HttpClient underlyingHttpClient = SphereAsyncHttpClientFactory.create();
        final MetricHttpClient httpClient = MetricHttpClient.of(underlyingHttpClient);
        final SphereAccessTokenSupplier tokenSupplier = SphereAccessTokenSupplierFactory.of(() -> httpClient)
                .createSupplierOfAutoRefresh(config);
        return SphereClient.of(config, httpClient, tokenSupplier);
    }

    private SphereClientConfig getSphereClientConfig() {
        final String project = requireNonNull(configuration.getString("sphere.project"));
        final String clientId = requireNonNull(configuration.getString("sphere.clientId"));
        final String clientSecret = requireNonNull(configuration.getString("sphere.clientSecret"));
        return SphereClientConfig.of(project, clientId, clientSecret);
    }
}
