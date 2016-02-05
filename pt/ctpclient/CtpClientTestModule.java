package ctpclient;

import com.google.inject.AbstractModule;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.client.SphereClient;

public class CtpClientTestModule extends AbstractModule {
    private final SphereClient sphereClient;

    public CtpClientTestModule(final SphereClient sphereClient) {
        this.sphereClient = sphereClient;
    }

    @Override
    protected void configure() {
        bind(SphereClient.class).toInstance(sphereClient);
        bind(PlayJavaSphereClient.class).toInstance(PlayJavaSphereClient.of(sphereClient));
    }
}