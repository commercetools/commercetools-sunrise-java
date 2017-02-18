package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRouteList;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionMyOrdersReverseRouter extends AbstractReflectionReverseRouter implements MyOrdersSimpleReverseRouter {

    private final ReverseCaller myOrderListPageCaller;
    private final ReverseCaller myOrderDetailPageCaller;

    @Inject
    private ReflectionMyOrdersReverseRouter(final ParsedRouteList parsedRouteList) {
        myOrderListPageCaller = getCallerForRoute(parsedRouteList, MY_ORDER_LIST_PAGE);
        myOrderDetailPageCaller = getCallerForRoute(parsedRouteList, MY_ORDER_DETAIL_PAGE);
    }

    @Override
    public Call myOrderListPageCall(final String languageTag) {
        return myOrderListPageCaller.call(languageTag);
    }

    @Override
    public Call myOrderDetailPageCall(final String languageTag, final String orderNumber) {
        return myOrderDetailPageCaller.call(languageTag, orderNumber);
    }
}
