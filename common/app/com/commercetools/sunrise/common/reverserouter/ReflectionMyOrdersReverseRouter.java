package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.pages.ParsedRoutes;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionMyOrdersReverseRouter extends ReflectionReverseRouterBase implements MyOrdersReverseRouter {

    private final ReverseCaller myOrderListPageCall;
    private final ReverseCaller myOrderDetailPageCall;

    @Inject
    private ReflectionMyOrdersReverseRouter(final ParsedRoutes parsedRoutes) {
        myOrderListPageCall = getCallerForRoute(parsedRoutes, "myOrderListPageCall");
        myOrderDetailPageCall = getCallerForRoute(parsedRoutes, "myOrderDetailPageCall");
    }

    @Override
    public Call myOrderListPageCall(final String languageTag) {
        return myOrderListPageCall.call(languageTag);
    }

    @Override
    public Call myOrderDetailPageCall(final String languageTag, final String orderNumber) {
        return myOrderDetailPageCall.call(languageTag, orderNumber);
    }
}
