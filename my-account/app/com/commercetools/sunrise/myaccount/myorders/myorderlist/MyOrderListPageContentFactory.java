package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;

import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

public class MyOrderListPageContentFactory {

    @Inject
    private OrderOverviewBeanFactory orderOverviewBeanFactory;

    public MyOrderListPageContent create(final PagedQueryResult<Order> orderQueryResult) {
        final MyOrderListPageContent content = new MyOrderListPageContent();
        fillOrders(content, orderQueryResult);
        return content;
    }

    protected void fillOrders(final MyOrderListPageContent content, final PagedQueryResult<Order> orderQueryResult) {
        content.setOrders(orderQueryResult.getResults().stream()
                .map(order -> orderOverviewBeanFactory.create(order))
                .collect(toList()));
    }

}
