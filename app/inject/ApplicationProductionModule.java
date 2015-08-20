package inject;

import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.templates.TemplateService;
import io.sphere.sdk.categories.CategoryTree;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import productcatalog.services.CategoryService;
import productcatalog.services.ProductProjectionService;
import productcatalog.services.ShippingMethodService;
import scala.collection.Seq;

import javax.inject.Singleton;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which
 * shall be used in production and integration tests.
 */
public class ApplicationProductionModule extends Module {

    @Override
    public Seq<Binding<?>> bindings(final Environment environment, final play.api.Configuration configuration) {
        return seq(
                bind(ProjectContext.class).toProvider(ProjectContextProvider.class).in(Singleton.class),
                bind(CategoryTree.class).toProvider(CategoryTreeProvider.class).in(Singleton.class),
                bind(TemplateService.class).toProvider(TemplateServiceProvider.class).in(Singleton.class),
                bind(CmsService.class).toProvider(CmsServiceProvider.class).in(Singleton.class),
                bind(CategoryService.class).toProvider(CategoryServiceProvider.class).in(Singleton.class),
                bind(ProductProjectionService.class).toProvider(ProductProjectionServiceProvider.class).in(Singleton.class),
                bind(ShippingMethodService.class).toProvider(ShippingMethodServiceProvider.class).in(Singleton.class)
        );
    }
}
