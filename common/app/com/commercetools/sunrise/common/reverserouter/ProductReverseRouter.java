package com.commercetools.sunrise.common.reverserouter;

import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Call;

import java.util.Locale;
import java.util.Optional;

public interface ProductReverseRouter {

    Call productDetailPageCall(final String languageTag, final String productSlug, final String sku);

    Call productOverviewPageCall(final String languageTag, final String categorySlug);

    Call processSearchProductsForm(final String languageTag);

    default Optional<Call> productDetailPageCall(final Locale locale, final ProductProjection product, final ProductVariant productVariant) {
        return product.getSlug().find(locale)
                .map(slug -> productDetailPageCall(locale.toLanguageTag(), slug, productVariant.getSku()));
    }

    default String productDetailPageUrlOrEmpty(final Locale locale, final ProductProjection product, final ProductVariant productVariant) {
        return productDetailPageCall(locale, product, productVariant).map(Call::url).orElse("");
    }

    default Optional<Call> productDetailPageCall(final Locale locale, final LineItem lineItem) {
        return Optional.ofNullable(lineItem.getProductSlug())
                .flatMap(slugs -> slugs.find(locale)
                        .map(slug -> productDetailPageCall(locale.toLanguageTag(), slug, lineItem.getVariant().getSku())));
    }

    default String productDetailPageUrlOrEmpty(final Locale locale, final LineItem lineItem) {
        return productDetailPageCall(locale, lineItem).map(Call::url).orElse("");
    }

    default Optional<Call> productOverviewPageCall(final Locale locale, final Category category) {
        return category.getSlug().find(locale)
                .map(slug -> productOverviewPageCall(locale.toLanguageTag(), slug));
    }

    default String productOverviewPageUrlOrEmpty(final Locale locale, final Category category) {
        return productOverviewPageCall(locale, category).map(Call::url).orElse("");
    }
}
