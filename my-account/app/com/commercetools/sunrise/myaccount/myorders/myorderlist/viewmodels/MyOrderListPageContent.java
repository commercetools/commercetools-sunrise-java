package com.commercetools.sunrise.myaccount.myorders.myorderlist.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import io.sphere.sdk.orders.Order;

import java.util.List;

public class MyOrderListPageContent extends PageContent {

    private List<Order> orders;

    public MyOrderListPageContent() {
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(final List<Order> orders) {
        this.orders = orders;
    }
}
