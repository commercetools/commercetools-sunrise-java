package com.commercetools.sunrise.common.reverserouter.productcatalog;

import com.commercetools.sunrise.common.reverserouter.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.common.reverserouter.ReverseCaller;
import com.commercetools.sunrise.framework.ParsedRouteList;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionProductReverseRouter extends AbstractReflectionReverseRouter implements ProductSimpleReverseRouter {

    private final ReverseCaller productDetailPageCaller;
    private final ReverseCaller productOverviewPageCaller;
    private final ReverseCaller processSearchProductsForm;

    @Inject
    private ReflectionProductReverseRouter(final ParsedRouteList parsedRouteList) {
        productDetailPageCaller = getCallerForRoute(parsedRouteList, "productDetailPageCall");
        productOverviewPageCaller = getCallerForRoute(parsedRouteList, "productOverviewPageCall");
        processSearchProductsForm = getCallerForRoute(parsedRouteList, "processSearchProductsForm");
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
