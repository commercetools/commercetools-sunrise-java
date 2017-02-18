package com.commercetools.sunrise.common.reverserouter.productcatalog;

import com.commercetools.sunrise.common.reverserouter.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Call;

import java.util.Optional;

@ImplementedBy(ReflectionProductLocalizedReverseRouter.class)
public interface ProductReverseRouter extends ProductSimpleReverseRouter, LocalizedReverseRouter {

    default Call productDetailPageCall(final String productSlug, final String sku) {
        return productDetailPageCall(languageTag(), productSlug, sku);
    }

    default Call productOverviewPageCall(final String categorySlug) {
        return productOverviewPageCall(languageTag(), categorySlug);
    }

    default Call processSearchProductsForm() {
        return processSearchProductsForm(languageTag());
    }

    default Optional<Call> productDetailPageCall(final ProductProjection product, final ProductVariant productVariant) {
        return productDetailPageCall(locale(), product, productVariant);
    }

    default String productDetailPageUrlOrEmpty(final ProductProjection product, final ProductVariant productVariant) {
        return productDetailPageUrlOrEmpty(locale(), product, productVariant);
    }

    default Optional<Call> productDetailPageCall(final LineItem lineItem) {
        return productDetailPageCall(locale(), lineItem);
    }

    default String productDetailPageUrlOrEmpty(final LineItem lineItem) {
        return productDetailPageUrlOrEmpty(locale(), lineItem);
    }

    default Optional<Call> productOverviewPageCall(final Category category) {
        return productOverviewPageCall(locale(), category);
    }

    default String productOverviewPageUrlOrEmpty(final Category category) {
        return productOverviewPageUrlOrEmpty(locale(), category);
    }
}
