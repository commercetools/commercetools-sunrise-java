package com.commercetools.sunrise.myaccount.myorders;

import com.commercetools.sunrise.common.pages.PageContent;

import java.util.List;

public class MyOrdersPageContent extends PageContent {

    private List<OrderOverviewBean> orders;

    public MyOrdersPageContent() {
    }

    public List<OrderOverviewBean> getOrders() {
        return orders;
    }

    public void setOrders(final List<OrderOverviewBean> orders) {
        this.orders = orders;
    }
}
