package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.models.PageContentFactory;

import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

public class MyOrderListPageContentFactory extends PageContentFactory<MyOrderListPageContent, MyOrderListControllerData> {

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
    public final MyOrderListPageContent create(final MyOrderListControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final MyOrderListPageContent model, final MyOrderListControllerData data) {
        super.initialize(model, data);
        fillOrders(model, data);
    }

    @Override
    protected void fillTitle(final MyOrderListPageContent model, final MyOrderListControllerData data) {

    }

    protected void fillOrders(final MyOrderListPageContent model, final MyOrderListControllerData data) {
        model.setOrders(data.getOrderQueryResult().getResults().stream()
                .map(orderOverviewBeanFactory::create)
                .collect(toList()));
    }
}
