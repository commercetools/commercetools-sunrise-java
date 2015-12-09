package inject;

import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.models.ProductDataConfig;
import common.controllers.ReverseRouter;
import common.templates.TemplateService;
import io.sphere.sdk.categories.CategoryTreeExtended;
import pages.ReverseRouterImpl;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import productcatalog.services.ProductService;
import purchase.ShippingMethods;
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
                bind(CategoryTreeExtended.class).toProvider(CategoryTreeProvider.class).in(Singleton.class),
                bind(ProductDataConfig.class).toProvider(ProductDataConfigProvider.class).in(Singleton.class),
                bind(TemplateService.class).toProvider(TemplateServiceProvider.class).in(Singleton.class),
                bind(CmsService.class).toProvider(CmsServiceProvider.class).in(Singleton.class),
                bind(ProductService.class).toProvider(ProductProjectionServiceProvider.class).in(Singleton.class),
                bind(ShippingMethods.class).toProvider(ShippingMethodsProvider.class).in(Singleton.class),
                bind(ReverseRouter.class).toInstance(new ReverseRouterImpl())
        );
    }
}
