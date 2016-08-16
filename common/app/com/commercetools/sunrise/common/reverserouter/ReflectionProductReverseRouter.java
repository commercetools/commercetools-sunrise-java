package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.pages.ParsedRoutes;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionProductReverseRouter extends ReflectionReverseRouterBase implements ProductReverseRouter {

    private ReverseCaller productDetailPageCaller;
    private ReverseCaller productOverviewPageCaller;
    private ReverseCaller processSearchProductsForm;

    @Inject
    private void setRoutes(final ParsedRoutes parsedRoutes) {
        productDetailPageCaller = getCallerForRoute(parsedRoutes, "productDetailPageCall");
        productOverviewPageCaller = getCallerForRoute(parsedRoutes, "productOverviewPageCall");
        processSearchProductsForm = getCallerForRoute(parsedRoutes, "processSearchProductsForm");
    }

    @Override
    public Call productDetailPageCall(final String languageTag, final String productSlug, final String sku) {
        return productDetailPageCaller.call(languageTag, productSlug, sku);
    }

    @Override
    public Call productOverviewPageCall(final String languageTag, final String categorySlug) {
        return productOverviewPageCaller.call(languageTag, categorySlug);
    }

    @Override
    public Call processSearchProductsForm(final String languageTag) {
        return processSearchProductsForm.call(languageTag);
    }
}
