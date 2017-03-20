package com.commercetools.sunrise.myaccount.myorders.myorderlist.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;

import java.util.List;

public class MyOrderListPageContent extends PageContent {

    private List<OrderOverviewViewModel> orders;

    public MyOrderListPageContent() {
    }

    public List<OrderOverviewViewModel> getOrders() {
        return orders;
    }

    public void setOrders(final List<OrderOverviewViewModel> orders) {
        this.orders = orders;
    }
}
