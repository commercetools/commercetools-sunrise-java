package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.pages.PageContent;

import java.util.List;

public class MyOrderListPageContent extends PageContent {

    private List<OrderOverviewBean> orders;

    public MyOrderListPageContent() {
    }

    public List<OrderOverviewBean> getOrders() {
        return orders;
    }

    public void setOrders(final List<OrderOverviewBean> orders) {
        this.orders = orders;
    }
}
