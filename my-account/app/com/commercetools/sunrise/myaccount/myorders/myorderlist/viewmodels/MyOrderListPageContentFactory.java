package com.commercetools.sunrise.myaccount.myorders.myorderlist.viewmodels;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.myaccount.myorders.myorderlist.OrderListWithCustomer;

import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

public class MyOrderListPageContentFactory extends PageContentFactory<MyOrderListPageContent, OrderListWithCustomer> {

    private final OrderOverviewViewModelFactory orderOverviewViewModelFactory;

    @Inject
    public MyOrderListPageContentFactory(final OrderOverviewViewModelFactory orderOverviewViewModelFactory) {
        this.orderOverviewViewModelFactory = orderOverviewViewModelFactory;
    }

    @Override
    protected MyOrderListPageContent getViewModelInstance(final OrderListWithCustomer orderListWithCustomer) {
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
        viewModel.setOrders(orderListWithCustomer.getOrders().getResults().stream()
                .map(orderOverviewViewModelFactory::create)
                .collect(toList()));
    }
}
