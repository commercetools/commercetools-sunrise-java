package plugins;

import com.google.inject.Provider;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientFactory;
import play.Configuration;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.F;

import javax.inject.Inject;
import javax.inject.Singleton;

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
        final String project = configuration.getString("sphere.project");
        final String clientId = configuration.getString("sphere.clientId");
        final String clientSecret = configuration.getString("sphere.clientSecret");
        final SphereClient sphereClient = SphereClientFactory.of().createClient(project, clientId, clientSecret);
        final PlayJavaSphereClient playJavaSphereClient = PlayJavaSphereClient.of(sphereClient);
        applicationLifecycle.addStopHook(() -> F.Promise.promise(() -> {
            playJavaSphereClient.close();
            return null;
        }));
        return playJavaSphereClient;
    }
}
