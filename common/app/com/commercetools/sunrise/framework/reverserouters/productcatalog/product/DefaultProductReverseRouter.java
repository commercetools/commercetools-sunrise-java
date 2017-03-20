package com.commercetools.sunrise.framework.reverserouters.productcatalog.product;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

@RequestScoped
public class DefaultProductReverseRouter extends AbstractLocalizedReverseRouter implements ProductReverseRouter {

    private final SimpleProductReverseRouter delegate;

    @Inject
    protected DefaultProductReverseRouter(final Locale locale, final SimpleProductReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call productDetailPageCall(final String languageTag, final String productIdentifier, final String productVariantIdentifier) {
        return delegate.productDetailPageCall(languageTag, productIdentifier, productVariantIdentifier);
    }

    @Override
    public Call productOverviewPageCall(final String languageTag, final String categoryIdentifier) {
        return delegate.productOverviewPageCall(languageTag, categoryIdentifier);
    }

    @Override
    public Call searchProcessCall(final String languageTag) {
        return delegate.searchProcessCall(languageTag);
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
     * It uses as category identifier the category slug for the current locale.
     */
    @Override
    public Optional<Call> productOverviewPageCall(final Category category) {
        return category.getSlug().find(locale())
                .map(this::productOverviewPageCall);
    }
}
