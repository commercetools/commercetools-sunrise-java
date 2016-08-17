package com.commercetools.sunrise.shoppingcart;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.queries.CartQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.UnaryOperator;

public interface CartFinder<C> {

    CompletionStage<Optional<Cart>> findCart(final C cartIdentifier,
                                             final UnaryOperator<CartQuery> runHookOnCartQuery);
}
