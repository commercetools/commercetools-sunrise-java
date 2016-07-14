package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;

import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

public class MyOrderListPageContentFactory extends Base {

    @Inject
    private OrderOverviewBeanFactory orderOverviewBeanFactory;

    public MyOrderListPageContent create(final PagedQueryResult<Order> orderQueryResult) {
        return fillBean(new MyOrderListPageContent(), orderQueryResult);
    }

    protected <T extends MyOrderListPageContent> T fillBean(final T bean, final PagedQueryResult<Order> orderQueryResult) {
        fillOrders(bean, orderQueryResult);
        return bean;
    }

    protected void fillOrders(final MyOrderListPageContent content, final PagedQueryResult<Order> orderQueryResult) {
        content.setOrders(orderQueryResult.getResults().stream()
                .map(order -> orderOverviewBeanFactory.create(order))
                .collect(toList()));
    }

}
