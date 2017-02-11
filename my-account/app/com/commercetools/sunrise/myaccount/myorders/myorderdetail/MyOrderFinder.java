package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyOrderFinder.class)
public interface MyOrderFinder {

    CompletionStage<Optional<Order>> findOrder(final Customer customer, final String identifier);

}
