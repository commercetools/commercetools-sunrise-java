package com.commercetools.sunrise.framework.reverserouters.productcatalog;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Call;

import java.util.Locale;
import java.util.Optional;

@ImplementedBy(ReflectionProductReverseRouter.class)
interface ProductSimpleReverseRouter {

    String PRODUCT_DETAIL_PAGE = "productDetailPageCall";

    Call productDetailPageCall(final String languageTag, final String productIdentifier, final String productVariantIdentifier);

    String PRODUCT_OVERVIEW_PAGE = "productOverviewPageCall";

    Call productOverviewPageCall(final String languageTag, final String categoryIdentifier);

    String SEARCH_PROCESS = "searchProcessCall";

    Call searchProcessCall(final String languageTag);

    default Optional<Call> productDetailPageCallByProductSlugAndSku(final Locale locale, final ProductProjection product, final ProductVariant productVariant) {
        return product.getSlug().find(locale)
                .map(slug -> productDetailPageCall(locale.toLanguageTag(), slug, productVariant.getSku()));
    }

    default Optional<Call> productDetailPageCallByProductSlugAndSku(final Locale locale, final LineItem lineItem) {
        return Optional.ofNullable(lineItem.getProductSlug())
                .flatMap(slugs -> slugs.find(locale)
                        .map(slug -> productDetailPageCall(locale.toLanguageTag(), slug, lineItem.getVariant().getSku())));
    }

    default Optional<Call> productOverviewPageCallByCategorySlug(final Locale locale, final Category category) {
        return category.getSlug().find(locale)
                .map(slug -> productOverviewPageCall(locale.toLanguageTag(), slug));
    }
}
