package com.commercetools.sunrise.myaccount.myorders;

import common.controllers.PageContent;

import java.util.List;

public class MyOrdersPageContent extends PageContent {

    private List<OrderOverviewBean> order;

    public MyOrdersPageContent() {
    }

    public List<OrderOverviewBean> getOrder() {
        return order;
    }

    public void setOrder(final List<OrderOverviewBean> order) {
        this.order = order;
    }
}
