package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.sessions.AbstractResourceInSession;
import com.commercetools.sunrise.core.sessions.StoringStrategy;
import io.sphere.sdk.carts.Cart;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Keeps some parts from the cart in session, such as cart ID and mini cart.
 */
@Singleton
final class MyCartInSessionImpl extends AbstractResourceInSession<Cart> implements MyCartInSession {

    @Inject
    MyCartInSessionImpl(final Configuration globalConfig, final StoringStrategy storingStrategy) {
        super(globalConfig.getConfig("sunrise.carts"), storingStrategy);
    }
}
