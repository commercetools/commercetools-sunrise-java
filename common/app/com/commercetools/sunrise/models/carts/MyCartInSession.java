package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.sessions.ResourceInSession;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Keeps some parts from the cart in session, such as cart ID and mini cart.
 */
@ImplementedBy(MyCartInSessionImpl.class)
public interface MyCartInSession extends ResourceInSession<Cart> {

    @Override
    Optional<String> findId();

    @Override
    Optional<Long> findVersion();

    @Override
    void store(@Nullable final Cart cart);

    @Override
    void remove();
}
