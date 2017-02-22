package com.commercetools.sunrise.framework.reverserouters.productcatalog;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Call;

import java.util.Optional;

@ImplementedBy(ReflectionProductLocalizedReverseRouter.class)
public interface ProductReverseRouter extends ProductSimpleReverseRouter, LocalizedReverseRouter {

    default Call productDetailPageCall(final String productIdentifier, final String productVariantIdentifier) {
        return productDetailPageCall(languageTag(), productIdentifier, productVariantIdentifier);
    }

    default Call productOverviewPageCall(final String categoryIdentifier) {
        return productOverviewPageCall(languageTag(), categoryIdentifier);
    }

    default Call searchProcessCall() {
        return searchProcessCall(languageTag());
    }

    default Optional<Call> productDetailPageCallByProductSlugAndSku(final ProductProjection product, final ProductVariant productVariant) {
        return productDetailPageCallByProductSlugAndSku(locale(), product, productVariant);
    }

    default Optional<Call> productDetailPageCallByProductSlugAndSku(final LineItem lineItem) {
        return productDetailPageCallByProductSlugAndSku(locale(), lineItem);
    }

    default Optional<Call> productOverviewPageCallByCategorySlug(final Category category) {
        return productOverviewPageCallByCategorySlug(locale(), category);
    }
}
