package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.framework.ParsedRouteList;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionMyOrdersReverseRouter extends AbstractReflectionReverseRouter implements MyOrdersSimpleReverseRouter {

    private final ReverseCaller myOrderListPageCall;
    private final ReverseCaller myOrderDetailPageCall;

    @Inject
    private ReflectionMyOrdersReverseRouter(final ParsedRouteList parsedRouteList) {
        myOrderListPageCall = getCallerForRoute(parsedRouteList, "myOrderListPageCall");
        myOrderDetailPageCall = getCallerForRoute(parsedRouteList, "myOrderDetailPageCall");
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
