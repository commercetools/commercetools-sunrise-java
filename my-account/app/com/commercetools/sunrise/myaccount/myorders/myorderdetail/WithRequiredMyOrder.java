package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface WithRequiredMyOrder {

    MyOrderFinder getMyOrderFinder();

    default CompletionStage<Result> requireMyOrder(final Customer customer, final String orderNumber, final Function<Order, CompletionStage<Result>> nextAction) {
        return getMyOrderFinder().apply(customer, orderNumber)
                .thenComposeAsync(orderOpt -> orderOpt
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundMyOrder),
                        HttpExecution.defaultContext());
    }

    CompletionStage<Result> handleNotFoundMyOrder();
}
