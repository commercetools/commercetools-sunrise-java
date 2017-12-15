package com.commercetools.sunrise.core.reverserouters.productcatalog.product;

import com.commercetools.sunrise.core.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.core.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.core.reverserouters.ReverseCaller;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Locale;
import java.util.Optional;

@Singleton
public class DefaultProductReverseRouter extends AbstractReflectionReverseRouter implements ProductReverseRouter {

    private final ReverseCaller productDetailPageCaller;
    private final ReverseCaller productOverviewPageCaller;
    private final ReverseCaller searchProcessCaller;
    private final Provider<Locale> localeProvider;

    @Inject
    protected DefaultProductReverseRouter(final ParsedRoutes parsedRoutes, final Provider<Locale> localeProvider) {
        productDetailPageCaller = getReverseCallerForSunriseRoute(PRODUCT_DETAIL_PAGE, parsedRoutes);
        productOverviewPageCaller = getReverseCallerForSunriseRoute(PRODUCT_OVERVIEW_PAGE, parsedRoutes);
        searchProcessCaller = getReverseCallerForSunriseRoute(SEARCH_PROCESS, parsedRoutes);
        this.localeProvider = localeProvider;
    }

    @Override
    public Call productDetailPageCall(final String productIdentifier, final String productVariantIdentifier) {
        return productDetailPageCaller.call(productIdentifier, productVariantIdentifier);
    }

    @Override
    public Call productOverviewPageCall(final String categoryIdentifier) {
        return productOverviewPageCaller.call(categoryIdentifier);
    }

    @Override
    public Call searchProcessCall() {
        return searchProcessCaller.call();
    }
    /**
     * {@inheritDoc}
     * It uses as product identifier the product slug for the current locale and as variant identifier the SKU.
     */
    @Override
    public Optional<Call> productDetailPageCall(final ProductProjection product, final ProductVariant productVariant) {
        return product.getSlug().find(locale())
                .map(slug -> productDetailPageCall(slug, productVariant.getSku()));
    }

    /**
     * {@inheritDoc}
     * It uses as product identifier the product slug for the current locale and as variant identifier the SKU.
     */
    @Override
    public Optional<Call> productDetailPageCall(final LineItem lineItem) {
        return Optional.ofNullable(lineItem.getProductSlug())
                .flatMap(slugs -> slugs.find(locale())
                        .map(slug -> productDetailPageCall(slug, lineItem.getVariant().getSku())));
    }

    /**
     * {@inheritDoc}
     * It uses as product identifier the product slug for the current locale and as variant identifier the SKU.
     */
    @Override
    public Optional<Call> productDetailPageCall(final io.sphere.sdk.shoppinglists.LineItem lineItem) {
        return Optional.ofNullable(lineItem.getProductSlug())
                .flatMap(slugs -> slugs.find(locale())
                        .flatMap(slug -> Optional.ofNullable(lineItem.getVariant())
                                .map(variant -> productDetailPageCall(slug, variant.getSku()))));
    }

    /**
     * {@inheritDoc}
     * It uses as category identifier the category slug for the current locale.
     */
    @Override
    public Optional<Call> productOverviewPageCall(final Category category) {
        return category.getSlug().find(locale())
                .map(this::productOverviewPageCall);
    }

    protected Locale locale() {
        return localeProvider.get();
    }
}
