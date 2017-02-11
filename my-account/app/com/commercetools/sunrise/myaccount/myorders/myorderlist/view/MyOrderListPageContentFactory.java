package com.commercetools.sunrise.myaccount.myorders.myorderlist.view;

import com.commercetools.sunrise.common.models.PageContentFactory;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;

import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

public class MyOrderListPageContentFactory extends PageContentFactory<MyOrderListPageContent, PagedQueryResult<Order>> {

    private final OrderOverviewBeanFactory orderOverviewBeanFactory;

    @Inject
    public MyOrderListPageContentFactory(final OrderOverviewBeanFactory orderOverviewBeanFactory) {
        this.orderOverviewBeanFactory = orderOverviewBeanFactory;
    }

    @Override
    protected MyOrderListPageContent getViewModelInstance() {
        return new MyOrderListPageContent();
    }

    @Override
    public final MyOrderListPageContent create(final PagedQueryResult<Order> orders) {
        return super.create(orders);
    }

    @Override
    protected final void initialize(final MyOrderListPageContent model, final PagedQueryResult<Order> orders) {
        super.initialize(model, orders);
        fillOrders(model, orders);
    }

    @Override
    protected void fillTitle(final MyOrderListPageContent model, final PagedQueryResult<Order> orders) {

    }

    protected void fillOrders(final MyOrderListPageContent model, final PagedQueryResult<Order> orders) {
        model.setOrders(orders.getResults().stream()
                .map(orderOverviewBeanFactory::create)
                .collect(toList()));
    }
}
