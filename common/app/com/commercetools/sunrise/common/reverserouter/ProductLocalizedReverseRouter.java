package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionProductReverseRouter.class)
public interface ProductLocalizedReverseRouter extends ProductReverseRouter, LocalizedReverseRouter {

    default Call productDetailPageCall(final String productSlug, final String sku) {
        return productDetailPageCall(languageTag(), productSlug, sku);
    }

    default Call productOverviewPageCall(final String categorySlug) {
        return productOverviewPageCall(languageTag(), categorySlug);
    }

    default Call processSearchProductsForm() {
        return processSearchProductsForm(languageTag());
    }
}
