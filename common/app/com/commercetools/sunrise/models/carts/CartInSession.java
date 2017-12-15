package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.sessions.ResourceStoringOperations;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Keeps some parts from the cart in session, such as cart ID and mini cart.
 */
@ImplementedBy(DefaultCartInSession.class)
public interface CartInSession extends ResourceStoringOperations<Cart> {

    Optional<String> findCartId();

    Optional<MiniCartViewModel> findMiniCart();

    @Override
    void store(@Nullable final Cart cart);

    @Override
    void remove();
}
