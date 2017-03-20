package com.commercetools.sunrise.framework.reverserouters.myaccount.myorders;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class SimpleMyOrdersReverseRouterByReflection extends AbstractReflectionReverseRouter implements SimpleMyOrdersReverseRouter {

    private final ReverseCaller myOrderListPageCaller;
    private final ReverseCaller myOrderDetailPageCaller;

    @Inject
    private SimpleMyOrdersReverseRouterByReflection(final ParsedRoutes parsedRoutes) {
        myOrderListPageCaller = getReverseCallerForSunriseRoute(MY_ORDER_LIST_PAGE, parsedRoutes);
        myOrderDetailPageCaller = getReverseCallerForSunriseRoute(MY_ORDER_DETAIL_PAGE, parsedRoutes);
    }

    @Override
    public Call myOrderListPageCall(final String languageTag) {
        return myOrderListPageCaller.call(languageTag);
    }

    @Override
    public Call myOrderDetailPageCall(final String languageTag, final String orderIdentifier) {
        return myOrderDetailPageCaller.call(languageTag, orderIdentifier);
    }
}
