package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import io.sphere.sdk.orders.Order;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface OrderFinder<T> {

    CompletionStage<Optional<Order>> findOrder(final T criteria);

}
