package tutorial;

import io.sphere.sdk.client.PlayJavaSphereClient;
import plugins.ProductionModule;

public final class ClientFactory {
    private ClientFactory() {
    }

    /**
     * Create a Sphere client for experiments in the console.
     * @return a new sphere client
     */
    @SuppressWarnings("unused")//it is not a part of the application
    public static PlayJavaSphereClient createClient() {
        return ProductionModule.createClient();
    }
}
