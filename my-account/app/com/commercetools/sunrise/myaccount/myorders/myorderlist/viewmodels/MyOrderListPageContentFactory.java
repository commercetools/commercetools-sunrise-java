package com.commercetools.sunrise.myaccount.myorders.myorderlist.viewmodels;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.OrderListWithCustomer;

import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

public class MyOrderListPageContentFactory extends PageContentFactory<MyOrderListPageContent, OrderListWithCustomer> {

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
    public final MyOrderListPageContent create(final OrderListWithCustomer orderListWithCustomer) {
        return super.create(orderListWithCustomer);
    }

    @Override
    protected final void initialize(final MyOrderListPageContent model, final OrderListWithCustomer orderListWithCustomer) {
        super.initialize(model, orderListWithCustomer);
        fillOrders(model, orderListWithCustomer);
    }

    @Override
    protected void fillTitle(final MyOrderListPageContent model, final OrderListWithCustomer orderListWithCustomer) {

    }

    protected void fillOrders(final MyOrderListPageContent model, final OrderListWithCustomer orderListWithCustomer) {
        model.setOrders(orderListWithCustomer.getOrders().getResults().stream()
                .map(orderOverviewBeanFactory::create)
                .collect(toList()));
    }
}
