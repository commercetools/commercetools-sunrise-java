package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;
import java.util.function.UnaryOperator;

public interface OrderListFinder<T> {

    CompletionStage<PagedQueryResult<Order>> findOrderList(final T criteria, final UnaryOperator<OrderQuery> filter);

}
