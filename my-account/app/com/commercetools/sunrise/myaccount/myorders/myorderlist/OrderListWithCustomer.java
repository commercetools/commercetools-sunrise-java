package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;

public final class OrderListWithCustomer {

    private final PagedQueryResult<Order> orders;
    private final Customer customer;

    private OrderListWithCustomer(final PagedQueryResult<Order> orders, final Customer customer) {
        this.orders = orders;
        this.customer = customer;
    }

    public PagedQueryResult<Order> getOrders() {
        return orders;
    }

    public Customer getCustomer() {
        return customer;
    }

    public static OrderListWithCustomer of(final PagedQueryResult<Order> orders, final Customer customer) {
        return new OrderListWithCustomer(orders, customer);
    }
}
