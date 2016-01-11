package inject;

import com.google.inject.AbstractModule;
import common.contexts.ProjectContext;
import common.models.ProductDataConfig;
import io.sphere.sdk.categories.CategoryTreeExtended;
import productcatalog.services.ProductService;
import shoppingcart.shipping.ShippingMethods;

import javax.inject.Singleton;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which
 * shall be used in production and integration tests.
 */
public class CtpModelsProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProjectContext.class).toProvider(ProjectContextProvider.class).in(Singleton.class);
        bind(CategoryTreeExtended.class).toProvider(CategoryTreeProvider.class).in(Singleton.class);
        bind(ProductDataConfig.class).toProvider(ProductDataConfigProvider.class).in(Singleton.class);
        bind(ProductService.class).toProvider(ProductServiceProvider.class).in(Singleton.class);
        bind(ShippingMethods.class).toProvider(ShippingMethodsProvider.class).in(Singleton.class);
    }
}
