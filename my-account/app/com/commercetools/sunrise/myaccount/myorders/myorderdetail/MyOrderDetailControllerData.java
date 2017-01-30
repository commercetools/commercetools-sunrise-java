package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.common.models.ControllerData;
import io.sphere.sdk.orders.Order;

public class MyOrderDetailControllerData extends ControllerData {

    private final Order order;

    public MyOrderDetailControllerData(final Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
