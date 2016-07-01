package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.UnaryOperator;

public interface OrderFinder<T> {

    CompletionStage<Optional<Order>> findOrder(final T criteria, final UnaryOperator<OrderQuery> filter);

}
