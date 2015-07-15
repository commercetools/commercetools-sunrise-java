package inject;

import com.google.inject.Provider;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.client.SphereClient;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.F;

import javax.inject.Inject;

public class PlayJavaSphereClientProvider implements Provider<PlayJavaSphereClient> {
    private final ApplicationLifecycle applicationLifecycle;
    private final SphereClient sphereClient;

    @Inject
    public PlayJavaSphereClientProvider(final ApplicationLifecycle applicationLifecycle, final SphereClient sphereClient) {
        this.applicationLifecycle = applicationLifecycle;
        this.sphereClient = sphereClient;
    }

    @Override
    public PlayJavaSphereClient get() {
        Logger.debug("Provide PlayJavaSphereClient");
        final PlayJavaSphereClient playJavaSphereClient = PlayJavaSphereClient.of(sphereClient);
        applicationLifecycle.addStopHook(() ->
                        F.Promise.promise(() -> {
                            playJavaSphereClient.close();
                            return null;
                        })
        );
        return playJavaSphereClient;
    }
}
