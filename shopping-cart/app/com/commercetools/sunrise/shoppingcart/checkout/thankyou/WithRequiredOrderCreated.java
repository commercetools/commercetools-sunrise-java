package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.models.orders.OrderFetcher;
import io.sphere.sdk.orders.Order;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface WithRequiredOrderCreated {

    OrderFetcher getOrderCreatedFinder();

    default CompletionStage<Result> requireOrderCreated(final Function<Order, CompletionStage<Result>> nextAction) {
        return getOrderCreatedFinder().get()
                .thenComposeAsync(orderOpt -> orderOpt
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundOrderCreated),
                        HttpExecution.defaultContext());
    }

    CompletionStage<Result> handleNotFoundOrderCreated();
}
