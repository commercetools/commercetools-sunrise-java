import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.common.categorytree.CategoryTreeInNewProvider;
import com.commercetools.sunrise.common.categorytree.RefreshableCategoryTree;
import com.commercetools.sunrise.common.contexts.CountryFromSessionProvider;
import com.commercetools.sunrise.common.contexts.CurrencyFromCountryProvider;
import com.commercetools.sunrise.common.contexts.LocaleFromUrlProvider;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.MetricSphereClientProvider;
import com.commercetools.sunrise.common.httpauth.HttpAuthentication;
import com.commercetools.sunrise.common.httpauth.basic.BasicAuthenticationProvider;
import com.commercetools.sunrise.common.localization.LocationSelectorControllerComponent;
import com.commercetools.sunrise.common.pages.DefaultPageNavMenuControllerComponent;
import com.commercetools.sunrise.common.search.facetedsearch.FacetedSearchConfigList;
import com.commercetools.sunrise.common.search.facetedsearch.FacetedSearchConfigListProvider;
import com.commercetools.sunrise.common.template.cms.FileBasedCmsServiceProvider;
import com.commercetools.sunrise.common.template.engine.HandlebarsTemplateEngineProvider;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.i18n.ConfigurableI18nResolverProvider;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.commercetools.sunrise.framework.MultiControllerComponentResolverBuilder;
import com.commercetools.sunrise.myaccount.CustomerComponent;
import com.commercetools.sunrise.shoppingcart.CartComponent;
import com.commercetools.sunrise.shoppingcart.MiniCartControllerComponent;
import com.commercetools.sunrise.shoppingcart.common.CheckoutCommonComponent;
import com.commercetools.sunrise.shoppingcart.common.CheckoutStepWidgetComponent;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeLocalRepository;
import io.sphere.sdk.producttypes.queries.ProductTypeQuery;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.money.CurrencyUnit;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;
import static io.sphere.sdk.queries.QueryExecutionUtils.queryAll;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(SphereClient.class).toProvider(MetricSphereClientProvider.class).in(RequestScoped.class);
        bindUserContext();
        bind(CmsService.class).toProvider(FileBasedCmsServiceProvider.class).in(Singleton.class);
        bind(TemplateEngine.class).toProvider(HandlebarsTemplateEngineProvider.class).in(Singleton.class);
        bind(I18nResolver.class).toProvider(ConfigurableI18nResolverProvider.class).in(Singleton.class);
        bind(HttpAuthentication.class).toProvider(BasicAuthenticationProvider.class).in(Singleton.class);
        bind(CategoryTree.class).annotatedWith(Names.named("new")).toProvider(CategoryTreeInNewProvider.class).in(Singleton.class);
        bind(FacetedSearchConfigList.class).toProvider(FacetedSearchConfigListProvider.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public CategoryTree provideRefreshableCategoryTree(@Named("global") final SphereClient sphereClient) {
        return RefreshableCategoryTree.of(sphereClient);
    }

    @Provides
    @Singleton
    private ProductTypeLocalRepository fetchProductTypeLocalRepository(@Named("global") final SphereClient sphereClient) {
        final ProductTypeQuery query = ProductTypeQuery.of();
        final List<ProductType> productTypes = blockingWait(queryAll(sphereClient, query), 30, TimeUnit.SECONDS);
        return ProductTypeLocalRepository.of(productTypes);
    }

    @Provides
    @RequestScoped
    public DateTimeFormatter dateTimeFormatter(final Locale locale) {
        return DateTimeFormatter.ofPattern("MMM d, yyyy", locale);
    }

    private void bindUserContext() {
        bind(Locale.class).toProvider(LocaleFromUrlProvider.class).in(RequestScoped.class);
        bind(CountryCode.class).toProvider(CountryFromSessionProvider.class).in(RequestScoped.class);
        bind(CurrencyUnit.class).toProvider(CurrencyFromCountryProvider.class).in(RequestScoped.class);
    }

    @Provides
    @RequestScoped
    public PriceSelection providePriceSelection(final CurrencyUnit currency, final CountryCode country) {
        return PriceSelection.of(currency)
                .withPriceCountry(country);
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
                .add(CartComponent.class, controller -> controller.getFrameworkTags().contains("shopping-cart") || controller.getFrameworkTags().contains("authentication"))
                .add(CustomerComponent.class, controller -> controller.getFrameworkTags().contains("my-account"))
                .build();
    }
}
