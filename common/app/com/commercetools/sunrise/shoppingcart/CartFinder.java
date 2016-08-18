package com.commercetools.sunrise.shoppingcart;

import io.sphere.sdk.carts.Cart;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface CartFinder<C> {

    CompletionStage<Optional<Cart>> findCart(final C cartIdentifier);
}
