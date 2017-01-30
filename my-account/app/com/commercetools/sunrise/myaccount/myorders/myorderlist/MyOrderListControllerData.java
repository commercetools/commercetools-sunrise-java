package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.models.ControllerData;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;

public class MyOrderListControllerData extends ControllerData {

    private final PagedQueryResult<Order> orderQueryResult;

    public MyOrderListControllerData(final PagedQueryResult<Order> orderQueryResult) {
        this.orderQueryResult = orderQueryResult;
    }

    public PagedQueryResult<Order> getOrderQueryResult() {
        return orderQueryResult;
    }
}
