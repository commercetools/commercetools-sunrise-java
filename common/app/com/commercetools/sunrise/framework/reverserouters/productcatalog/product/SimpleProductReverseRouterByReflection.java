package com.commercetools.sunrise.framework.reverserouters.productcatalog.product;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class SimpleProductReverseRouterByReflection extends AbstractReflectionReverseRouter implements SimpleProductReverseRouter {

    private final ReverseCaller productDetailPageCaller;
    private final ReverseCaller productOverviewPageCaller;
    private final ReverseCaller searchProcessCaller;

    @Inject
    private SimpleProductReverseRouterByReflection(final ParsedRoutes parsedRoutes) {
        productDetailPageCaller = getReverseCallerForSunriseRoute(PRODUCT_DETAIL_PAGE, parsedRoutes);
        productOverviewPageCaller = getReverseCallerForSunriseRoute(PRODUCT_OVERVIEW_PAGE, parsedRoutes);
        searchProcessCaller = getReverseCallerForSunriseRoute(SEARCH_PROCESS, parsedRoutes);
    }

    @Override
    public Call productDetailPageCall(final String languageTag, final String productIdentifier, final String productVariantIdentifier) {
        return productDetailPageCaller.call(languageTag, productIdentifier, productVariantIdentifier);
    }

    @Override
    public Call productOverviewPageCall(final String languageTag, final String categoryIdentifier) {
        return productOverviewPageCaller.call(languageTag, categoryIdentifier);
    }

    @Override
    public Call searchProcessCall(final String languageTag) {
        return searchProcessCaller.call(languageTag);
    }
}
