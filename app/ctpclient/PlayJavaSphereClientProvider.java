package ctpclient;

import com.google.inject.Provider;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.client.SphereClient;
import play.Logger;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

class PlayJavaSphereClientProvider implements Provider<PlayJavaSphereClient> {
    private final ApplicationLifecycle applicationLifecycle;
    private final SphereClient sphereClient;

    @Inject
    public PlayJavaSphereClientProvider(final ApplicationLifecycle applicationLifecycle, final SphereClient sphereClient) {
        this.applicationLifecycle = applicationLifecycle;
        this.sphereClient = sphereClient;
    }

    @Override
    public PlayJavaSphereClient get() {
        Logger.info("Provide PlayJavaSphereClient");
        final PlayJavaSphereClient playJavaSphereClient = PlayJavaSphereClient.of(sphereClient);
        applicationLifecycle.addStopHook(() ->
                CompletableFuture.runAsync(() -> playJavaSphereClient.close())
        );
        return playJavaSphereClient;
    }
}
