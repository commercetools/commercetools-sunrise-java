package inject;

import com.neovisionaries.i18n.CountryCode;
import countries.CountryOperations;
import generalpages.controllers.ViewService;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;

import javax.inject.Singleton;

import play.api.Environment;
import play.api.inject.*;
import scala.collection.Seq;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which
 * shall be used in production and integration tests.
 */
public class ProductionModule extends Module {

    @Override
    public Seq<Binding<?>> bindings(final Environment environment, final play.api.Configuration configuration) {
        return seq(
                bind(CountryCode.class).qualifiedWith("default").toInstance(CountryOperations.of(new play.Configuration(configuration)).defaultCountry()),//check on start
                bind(PlayJavaSphereClient.class).toProvider(PlayJavaSphereClientProvider.class).in(Singleton.class),
                bind(CategoryTree.class).toProvider(CategoryTreeProvider.class),
                bind(ViewService.class).toInstance(ViewService.of())
        );
    }
}
