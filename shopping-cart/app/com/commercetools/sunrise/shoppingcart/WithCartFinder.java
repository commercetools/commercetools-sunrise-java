package com.commercetools.sunrise.shoppingcart;

import io.sphere.sdk.carts.Cart;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface WithCartFinder {

    CartFinder getCartFinder();

    default CompletionStage<Result> requireNonEmptyCart(final Function<Cart, CompletionStage<Result>> nextAction) {
        return getCartFinder().get()
                .thenComposeAsync(cartOpt -> cartOpt
                                .filter(cart -> !cart.getLineItems().isEmpty())
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundCart),
                        HttpExecution.defaultContext());
    }

    CompletionStage<Result> handleNotFoundCart();
}
