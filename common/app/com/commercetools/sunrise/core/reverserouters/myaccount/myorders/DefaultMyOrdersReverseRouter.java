package com.commercetools.sunrise.core.reverserouters.myaccount.myorders;

import com.commercetools.sunrise.core.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.core.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.core.reverserouters.ReverseCaller;
import io.sphere.sdk.orders.Order;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class DefaultMyOrdersReverseRouter extends AbstractReflectionReverseRouter implements MyOrdersReverseRouter {

    private final ReverseCaller myOrderListPageCaller;
    private final ReverseCaller myOrderDetailPageCaller;

    @Inject
    protected DefaultMyOrdersReverseRouter(final ParsedRoutes parsedRoutes) {
        myOrderListPageCaller = getReverseCallerForSunriseRoute(MY_ORDER_LIST_PAGE, parsedRoutes);
        myOrderDetailPageCaller = getReverseCallerForSunriseRoute(MY_ORDER_DETAIL_PAGE, parsedRoutes);
    }

    @Override
    public Call myOrderListPageCall() {
        return myOrderListPageCaller.call();
    }

    @Override
    public Call myOrderDetailPageCall(final String orderIdentifier) {
        return myOrderDetailPageCaller.call(orderIdentifier);
    }

    /**
     * {@inheritDoc}
     * It uses as order identifier the order number.
     */
    @Override
    public Optional<Call> myOrderDetailPageCall(final Order order) {
        return Optional.ofNullable(order.getOrderNumber())
                .map(this::myOrderDetailPageCall);
    }
}
