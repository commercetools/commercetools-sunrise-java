package com.commercetools.sunrise.myaccount.myorders.myorderlist.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContentFactory;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.OrderListWithCustomer;

import javax.inject.Inject;

public class MyOrderListPageContentFactory extends PageContentFactory<MyOrderListPageContent, OrderListWithCustomer> {

    @Inject
    public MyOrderListPageContentFactory() {
    }

    @Override
    protected MyOrderListPageContent newViewModelInstance(final OrderListWithCustomer orderListWithCustomer) {
        return new MyOrderListPageContent();
    }

    @Override
    public final MyOrderListPageContent create(final OrderListWithCustomer orderListWithCustomer) {
        return super.create(orderListWithCustomer);
    }

    @Override
    protected final void initialize(final MyOrderListPageContent viewModel, final OrderListWithCustomer orderListWithCustomer) {
        super.initialize(viewModel, orderListWithCustomer);
        fillOrders(viewModel, orderListWithCustomer);
    }

    @Override
    protected void fillTitle(final MyOrderListPageContent viewModel, final OrderListWithCustomer orderListWithCustomer) {

    }

    protected void fillOrders(final MyOrderListPageContent viewModel, final OrderListWithCustomer orderListWithCustomer) {
        viewModel.setOrders(orderListWithCustomer.getOrders().getResults());
    }
}
