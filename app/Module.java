import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.common.categorytree.CategoryTreeInNewProvider;
import com.commercetools.sunrise.common.categorytree.RefreshableCategoryTree;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.RequestScopedSphereClientProvider;
import com.commercetools.sunrise.common.httpauth.HttpAuthentication;
import com.commercetools.sunrise.common.httpauth.basic.BasicAuthenticationProvider;
import com.commercetools.sunrise.common.localization.LocationSelectorControllerComponent;
import com.commercetools.sunrise.common.pages.DefaultPageNavMenuControllerComponent;
import com.commercetools.sunrise.common.template.cms.FileBasedCmsServiceProvider;
import com.commercetools.sunrise.common.template.engine.HandlebarsTemplateEngineProvider;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.i18n.ConfigurableI18nResolverProvider;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.commercetools.sunrise.framework.MultiControllerComponentResolverBuilder;
import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.FacetedSearchConfigList;
import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.FacetedSearchConfigListProvider;
import com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage.ProductsPerPageConfig;
import com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage.ProductsPerPageConfigProvider;
import com.commercetools.sunrise.productcatalog.productoverview.search.sort.SortConfig;
import com.commercetools.sunrise.productcatalog.productoverview.search.sort.SortConfigProvider;
import com.commercetools.sunrise.shoppingcart.MiniCartControllerComponent;
import com.commercetools.sunrise.shoppingcart.common.CheckoutCommonComponent;
import com.commercetools.sunrise.shoppingcart.common.CheckoutStepWidgetComponent;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Named;
import javax.inject.Singleton;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(SphereClient.class).toProvider(RequestScopedSphereClientProvider.class).in(RequestScoped.class);
        bind(CmsService.class).toProvider(FileBasedCmsServiceProvider.class).in(Singleton.class);
        bind(TemplateEngine.class).toProvider(HandlebarsTemplateEngineProvider.class).in(Singleton.class);
        bind(I18nResolver.class).toProvider(ConfigurableI18nResolverProvider.class).in(Singleton.class);
        bind(HttpAuthentication.class).toProvider(BasicAuthenticationProvider.class).in(Singleton.class);
        bind(CategoryTree.class).annotatedWith(Names.named("new")).toProvider(CategoryTreeInNewProvider.class).in(Singleton.class);
        bind(ProductsPerPageConfig.class).toProvider(ProductsPerPageConfigProvider.class).in(Singleton.class);
        bind(SortConfig.class).toProvider(SortConfigProvider.class).in(Singleton.class);
        bind(FacetedSearchConfigList.class).toProvider(FacetedSearchConfigListProvider.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public CategoryTree provideRefreshableCategoryTree(@Named("global") final SphereClient sphereClient) {
        return RefreshableCategoryTree.of(sphereClient);
    }

    @Provides
    public MultiControllerComponentResolver provideMultiControllerComponentResolver() {
        //here are also instanceof checks possible
        return new MultiControllerComponentResolverBuilder()
                .add(CheckoutCommonComponent.class, controller -> controller.getFrameworkTags().contains("checkout"))
                .add(CheckoutStepWidgetComponent.class, controller -> controller.getFrameworkTags().contains("checkout"))
                .add(MiniCartControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .add(DefaultPageNavMenuControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .add(LocationSelectorControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .build();
    }
}
