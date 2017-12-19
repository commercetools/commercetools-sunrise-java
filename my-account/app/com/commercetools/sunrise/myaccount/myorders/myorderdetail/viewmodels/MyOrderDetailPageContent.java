package com.commercetools.sunrise.myaccount.myorders.myorderdetail.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import io.sphere.sdk.orders.Order;

public class MyOrderDetailPageContent extends PageContent {

    private Order order;

    public MyOrderDetailPageContent() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }
}
