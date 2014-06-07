package tutorial;

import com.typesafe.config.ConfigFactory;
import io.sphere.sdk.client.PlayJavaClient;
import io.sphere.sdk.client.PlayJavaClientImpl;

public final class ClientFactory {
    private ClientFactory() {
    }

    /**
     * Create a Sphere client for experiments in the console.
     * @return a new sphere client
     */
    @SuppressWarnings("unused")//it is not a part of the application
    public static PlayJavaClient createClient() {
        return new PlayJavaClientImpl(ConfigFactory.load());
    }
}
