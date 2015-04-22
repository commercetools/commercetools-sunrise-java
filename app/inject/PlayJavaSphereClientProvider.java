package inject;

import com.google.inject.Provider;
import io.sphere.sdk.client.*;
import io.sphere.sdk.play.metrics.MetricHttpClient;
import play.Configuration;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.F;

import javax.inject.Inject;
import javax.inject.Singleton;

import static java.util.Objects.requireNonNull;

@Singleton
class PlayJavaSphereClientProvider implements Provider<PlayJavaSphereClient> {

    private final Configuration configuration;
    private final ApplicationLifecycle applicationLifecycle;

    @Inject
    public PlayJavaSphereClientProvider(final Configuration configuration, final ApplicationLifecycle applicationLifecycle) {
        this.configuration = configuration;
        this.applicationLifecycle = applicationLifecycle;
    }

    @Override
    public PlayJavaSphereClient get() {
        Logger.info("execute PlayJavaSphereClientProvider.get()");
        final SphereClientConfig config = getSphereClientConfig();
        final SphereClient sphereClient = createSphereClient(config);
        final PlayJavaSphereClient playJavaSphereClient = PlayJavaSphereClient.of(sphereClient);
        applicationLifecycle.addStopHook(() -> F.Promise.promise(() -> {
            playJavaSphereClient.close();
            return null;
        }));
        return playJavaSphereClient;
    }

    private SphereClient createSphereClient(SphereClientConfig config) {
        final MetricHttpClient httpClient = MetricHttpClient.of(NingHttpClientAdapter.of());
        final SphereAccessTokenSupplier tokenSupplier = SphereAccessTokenSupplierFactory.of().createSupplierOfAutoRefresh(config);
        return SphereClient.of(config, httpClient, tokenSupplier);
    }

    private SphereClientConfig getSphereClientConfig() {
        final String project = requireNonNull(configuration.getString("sphere.project"));
        final String clientId = requireNonNull(configuration.getString("sphere.clientId"));
        final String clientSecret = requireNonNull(configuration.getString("sphere.clientSecret"));
        return SphereClientConfig.of(project, clientId, clientSecret);
    }
}
