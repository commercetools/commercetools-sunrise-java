package com.commercetools.sunrise.shoppingcart;

import io.sphere.sdk.carts.Cart;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface WithRequiredCart {

    CartFinder getCartFinder();

    default CompletionStage<Result> requireCart(final Function<Cart, CompletionStage<Result>> nextAction) {
        return getCartFinder().get()
                .thenComposeAsync(cartOpt -> cartOpt
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundCart),
                        HttpExecution.defaultContext());
    }

    default CompletionStage<Result> requireNonEmptyCart(final Function<Cart, CompletionStage<Result>> nextAction) {
        return requireCart(cart -> {
            if (cart.getLineItems().isEmpty()) {
                return handleNotFoundCart();
            } else {
                return nextAction.apply(cart);
            }
        });
    }

    CompletionStage<Result> handleNotFoundCart();
}
