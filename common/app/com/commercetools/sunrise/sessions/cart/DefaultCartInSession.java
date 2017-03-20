package com.commercetools.sunrise.sessions.cart;

import com.commercetools.sunrise.framework.viewmodels.content.carts.MiniCartViewModel;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.sessions.DataFromResourceStoringOperations;
import com.commercetools.sunrise.sessions.ObjectStoringSessionStrategy;
import com.commercetools.sunrise.framework.viewmodels.content.carts.MiniCartViewModelFactory;
import io.sphere.sdk.carts.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Keeps some parts from the cart in session, such as cart ID and mini cart.
 */
@RequestScoped
public class DefaultCartInSession extends DataFromResourceStoringOperations<Cart> implements CartInSession {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartInSession.class);
    private static final String DEFAULT_CART_ID_SESSION_KEY = "sunrise-cart-id";
    private static final String DEFAULT_MINI_CART_SESSION_KEY = "sunrise-mini-cart";

    private final String cartIdSessionKey;
    private final String miniCartSessionKey;
    private final ObjectStoringSessionStrategy session;
    private final MiniCartViewModelFactory miniCartViewModelFactory;

    @Inject
    public DefaultCartInSession(final Configuration configuration, final ObjectStoringSessionStrategy session, final MiniCartViewModelFactory miniCartViewModelFactory) {
        this.cartIdSessionKey = configuration.getString("session.cart.cartId", DEFAULT_CART_ID_SESSION_KEY);
        this.miniCartSessionKey = configuration.getString("session.cart.miniCart", DEFAULT_MINI_CART_SESSION_KEY);
        this.session = session;
        this.miniCartViewModelFactory = miniCartViewModelFactory;
    }

    @Override
    protected final Logger getLogger() {
        return LOGGER;
    }

    protected final ObjectStoringSessionStrategy getSession() {
        return session;
    }

    @Override
    public Optional<String> findCartId() {
        return session.findValueByKey(cartIdSessionKey);
    }

    @Override
    public Optional<MiniCartViewModel> findMiniCart() {
        return session.findObjectByKey(miniCartSessionKey, MiniCartViewModel.class);
    }

    @Override
    public void store(@Nullable final Cart cart) {
        super.store(cart);
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    protected void storeAssociatedData(final Cart cart) {
        session.overwriteObjectByKey(miniCartSessionKey, miniCartViewModelFactory.create(cart));
        session.overwriteValueByKey(cartIdSessionKey, cart.getId());
    }

    @Override
    protected void removeAssociatedData() {
        session.removeObjectByKey(miniCartSessionKey);
        session.removeValueByKey(cartIdSessionKey);
    }
}
