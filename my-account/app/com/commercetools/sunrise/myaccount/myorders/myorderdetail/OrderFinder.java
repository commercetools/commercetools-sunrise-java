package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(OrderFinderByCustomerAndOrderNumber.class)
public interface OrderFinder {

    CompletionStage<Optional<Order>> findOrder(final String identifier);

}
