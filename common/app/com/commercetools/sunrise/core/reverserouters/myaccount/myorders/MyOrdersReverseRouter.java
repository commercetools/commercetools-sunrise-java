package com.commercetools.sunrise.core.reverserouters.myaccount.myorders;

import com.commercetools.sunrise.core.reverserouters.ReverseRouter;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;
import play.mvc.Call;

import java.util.Optional;

@ImplementedBy(DefaultMyOrdersReverseRouter.class)
public interface MyOrdersReverseRouter extends ReverseRouter {

    String MY_ORDER_LIST_PAGE = "myOrderListPageCall";

    Call myOrderListPageCall();

    String MY_ORDER_DETAIL_PAGE = "myOrderDetailPageCall";

    Call myOrderDetailPageCall(final String orderIdentifier);

    /**
     * Finds the call to access the order detail page of the given order.
     * @param order the order that we want to access via the page call
     * @return the page call to access the detail page of this order
     */
    Optional<Call> myOrderDetailPageCall(final Order order);
}
