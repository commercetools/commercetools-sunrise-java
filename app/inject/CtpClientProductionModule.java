package inject;

import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.client.SphereClient;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

import javax.inject.Singleton;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which
 * shall be used in production and integration tests.
 */
public class CtpClientProductionModule extends Module {

    @Override
    public Seq<Binding<?>> bindings(final Environment environment, final play.api.Configuration configuration) {
        return seq(
                bind(SphereClient.class).toProvider(SphereClientProvider.class).in(Singleton.class),
                bind(PlayJavaSphereClient.class).toProvider(PlayJavaSphereClientProvider.class).in(Singleton.class));
    }
}
