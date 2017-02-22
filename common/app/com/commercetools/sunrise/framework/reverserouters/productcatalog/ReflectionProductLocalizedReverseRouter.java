package com.commercetools.sunrise.framework.reverserouters.productcatalog;

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
final class ReflectionProductLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements ProductReverseRouter {

    private final ProductSimpleReverseRouter delegate;

    @Inject
    private ReflectionProductLocalizedReverseRouter(final Locale locale, final ProductSimpleReverseRouter reverseRouter) {
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

    @Override
    public Optional<Call> productDetailPageCallByProductSlugAndSku(final Locale locale, final ProductProjection product, final ProductVariant productVariant) {
        return delegate.productDetailPageCallByProductSlugAndSku(locale, product, productVariant);
    }

    @Override
    public Optional<Call> productDetailPageCallByProductSlugAndSku(final Locale locale, final LineItem lineItem) {
        return delegate.productDetailPageCallByProductSlugAndSku(locale, lineItem);
    }

    @Override
    public Optional<Call> productOverviewPageCallByCategorySlug(final Locale locale, final Category category) {
        return delegate.productOverviewPageCallByCategorySlug(locale, category);
    }
}
