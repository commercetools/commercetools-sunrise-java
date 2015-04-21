package plugins;

import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;

import javax.inject.Singleton;

import play.api.*;
import play.api.inject.*;
import scala.collection.Seq;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which
 * shall be used in production and integration tests.
 */
public class ProductionModule extends Module {

    @Override
    public Seq<Binding<?>> bindings(Environment environment, play.api.Configuration configuration) {
        return seq(
                bind(PlayJavaSphereClient.class).toProvider(PlayJavaSphereClientProvider.class).in(Singleton.class),
                bind(CategoryTree.class).toProvider(CategoryTreeProvider.class).eagerly()//eagerly to fetch categories at start and not at first request
        );
    }
}
