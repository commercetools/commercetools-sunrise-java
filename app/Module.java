import com.commercetools.sunrise.cms.CmsComponent;
import com.commercetools.sunrise.core.hooks.GlobalComponentRegistry;
import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.viewmodels.meta.TemplateComponent;
import com.commercetools.sunrise.email.EmailSender;
import com.commercetools.sunrise.httpauth.HttpAuthentication;
import com.commercetools.sunrise.httpauth.basic.BasicAuthenticationProvider;
import com.commercetools.sunrise.models.carts.CartComponentSupplier;
import com.commercetools.sunrise.models.categories.CategoryTreeDisplayingComponent;
import com.commercetools.sunrise.models.customers.CustomerComponentSupplier;
import com.commercetools.sunrise.models.customers.MyCustomerInSession;
import com.commercetools.sunrise.models.products.ProductListFetcher;
import com.commercetools.sunrise.models.products.ProductListFetcherWithMatchingVariants;
import com.commercetools.sunrise.models.search.facetedsearch.terms.viewmodels.AlphabeticallySortedTermFacetViewModelFactory;
import com.commercetools.sunrise.models.search.facetedsearch.terms.viewmodels.CustomSortedTermFacetViewModelFactory;
import com.commercetools.sunrise.models.search.facetedsearch.terms.viewmodels.TermFacetViewModelFactory;
import com.commercetools.sunrise.models.shoppinglists.WishlistComponentSupplier;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.neovisionaries.i18n.CountryCode;
import email.smtp.EmailSenderProvider;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeLocalRepository;
import io.sphere.sdk.producttypes.queries.ProductTypeQuery;

import javax.inject.Singleton;
import javax.money.CurrencyUnit;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;
import static io.sphere.sdk.queries.QueryExecutionUtils.queryAll;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule {

    @Override
    protected void configure() {
        // Binding for the HTTP Authentication
        bind(HttpAuthentication.class)
                .toProvider(BasicAuthenticationProvider.class)
                .in(Singleton.class);

        // Bindings fo email sender
        bind(EmailSender.class)
                .toProvider(EmailSenderProvider.class)
                .in(Singleton.class);

        // Bindings for the configured faceted search mappers
        bind(TermFacetViewModelFactory.class)
                .annotatedWith(Names.named("alphabeticallySorted"))
                .to(AlphabeticallySortedTermFacetViewModelFactory.class)
                .in(RequestScoped.class);
        bind(TermFacetViewModelFactory.class)
                .annotatedWith(Names.named("customSorted"))
                .to(CustomSortedTermFacetViewModelFactory.class)
                .in(RequestScoped.class);

        // Binding to enable matching variants on listing products
        // IMPORTANT: comment the following line if your project does not require this functionality, leaving it on can severely affect performance
        bind(ProductListFetcher.class).to(ProductListFetcherWithMatchingVariants.class);

        // Provide here your own bindings
    }


    @Provides
    @Singleton
    private ProductTypeLocalRepository fetchProductTypeLocalRepository(final SphereClient sphereClient) {
        final ProductTypeQuery query = ProductTypeQuery.of();
        final List<ProductType> productTypes = blockingWait(queryAll(sphereClient, query), 1, TimeUnit.MINUTES);
        return ProductTypeLocalRepository.of(productTypes);
    }

    @Provides
    @RequestScoped
    public DateTimeFormatter dateTimeFormatter(final Locale locale) {
        return DateTimeFormatter.ofPattern("MMM d, yyyy", locale);
    }

    @Provides
    @RequestScoped
    public PriceSelection providePriceSelection(final CurrencyUnit currency, final CountryCode country,
                                                final MyCustomerInSession myCustomerInSession) {
        return PriceSelection.of(currency)
                .withPriceCountry(country)
                .withPriceCustomerGroupId(myCustomerInSession.findCustomerGroupId().orElse(null));
    }

    @Provides
    @Singleton
    public GlobalComponentRegistry provideGlobalComponentRegistry() {
        final GlobalComponentRegistry globalComponentRegistry = new GlobalComponentRegistry();
        globalComponentRegistry.add(TemplateComponent.class);
        globalComponentRegistry.addAll(CartComponentSupplier.get());
        globalComponentRegistry.addAll(CustomerComponentSupplier.get());
        globalComponentRegistry.addAll(WishlistComponentSupplier.get());
        globalComponentRegistry.add(CategoryTreeDisplayingComponent.class);
        globalComponentRegistry.add(CmsComponent.class);
        return globalComponentRegistry;
    }
}
