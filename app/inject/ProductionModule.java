package inject;

import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.templates.TemplateService;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.client.SphereClient;
import productcatalog.models.ShippingMethods;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import productcatalog.services.CategoryService;
import productcatalog.services.CategoryServiceImpl;
import productcatalog.services.ProductProjectionService;
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
                bind(SphereClient.class).toProvider(SphereClientProvider.class).in(Singleton.class),
                bind(PlayJavaSphereClient.class).toProvider(PlayJavaSphereClientProvider.class).in(Singleton.class),
                bind(ProjectContext.class).toProvider(ProjectContextProvider.class).in(Singleton.class),
                bind(CategoryTree.class).toProvider(CategoryTreeProvider.class).in(Singleton.class),
                bind(TemplateService.class).toProvider(TemplateServiceProvider.class).in(Singleton.class),
                bind(CmsService.class).toProvider(CmsServiceProvider.class).in(Singleton.class),
                bind(ShippingMethods.class).toProvider(ShippingMethodProvider.class).in(Singleton.class),
                bind(CategoryService.class).toProvider(CategoryServiceProvider.class).in(Singleton.class),
                bind(ProductProjectionService.class).toProvider(ProductProjectionServiceProvider.class).in(Singleton.class)
        );
    }
}
