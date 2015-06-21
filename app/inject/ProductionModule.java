package inject;

import com.neovisionaries.i18n.CountryCode;
import common.cms.CmsService;
import common.countries.CountryOperations;
import common.templates.TemplateService;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

import javax.inject.Singleton;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which
 * shall be used in production and integration tests.
 */
public class ProductionModule extends Module {

    @Override
    public Seq<Binding<?>> bindings(final Environment environment, final play.api.Configuration configuration) {
        return seq(
                bind(CountryCode.class).qualifiedWith("default").toInstance(defaultCountry(configuration)), // checks on start
                bind(PlayJavaSphereClient.class).toProvider(PlayJavaSphereClientProvider.class).in(Singleton.class),
                bind(CategoryTree.class).toProvider(CategoryTreeProvider.class),
                bind(TemplateService.class).toProvider(TemplateServiceProvider.class),
                bind(CmsService.class).toProvider(CmsServiceProvider.class)
        );
    }

    private CountryCode defaultCountry(final Configuration configuration) {
        return CountryOperations.of(new play.Configuration(configuration)).defaultCountry();
    }
}
