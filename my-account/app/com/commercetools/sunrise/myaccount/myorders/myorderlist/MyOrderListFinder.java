package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyOrderListFinder.class)
public interface MyOrderListFinder {

    CompletionStage<PagedQueryResult<Order>> findOrders(final Customer customer);

}
