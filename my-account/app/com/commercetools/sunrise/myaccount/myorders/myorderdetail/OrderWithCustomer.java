package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.framework.SunriseModel;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;

public final class OrderWithCustomer extends SunriseModel {

    private final Order order;
    private final Customer customer;

    private OrderWithCustomer(final Order order, final Customer customer) {
        this.order = order;
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public static OrderWithCustomer of(final Order order, final Customer customer) {
        return new OrderWithCustomer(order, customer);
    }
}
