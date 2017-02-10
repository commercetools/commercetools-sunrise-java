package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;

@ImplementedBy(OrderListFinderByCustomer.class)
public interface OrderListFinder {

    CompletionStage<PagedQueryResult<Order>> findOrders();

}
