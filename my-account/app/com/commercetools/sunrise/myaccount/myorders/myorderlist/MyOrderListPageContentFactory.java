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
        final MyOrderListPageContent bean = new MyOrderListPageContent();
        initialize(bean, orderQueryResult);
        return bean;
    }

    protected final void initialize(final MyOrderListPageContent bean, final PagedQueryResult<Order> orderQueryResult) {
        fillOrders(bean, orderQueryResult);
    }

    protected void fillOrders(final MyOrderListPageContent bean, final PagedQueryResult<Order> orderQueryResult) {
        bean.setOrders(orderQueryResult.getResults().stream()
                .map(order -> orderOverviewBeanFactory.create(order))
                .collect(toList()));
    }

}
