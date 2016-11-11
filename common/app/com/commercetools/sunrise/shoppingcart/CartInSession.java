package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.sessions.DataFromResourceStoringOperations;
import com.commercetools.sunrise.common.sessions.ObjectStoringSessionStrategy;
import com.google.inject.Injector;
import io.sphere.sdk.carts.Cart;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Keeps some parts from the cart in session, such as cart ID and mini cart.
 */
@RequestScoped
public class CartInSession extends DataFromResourceStoringOperations<Cart> {

    private static final String DEFAULT_CART_ID_SESSION_KEY = "sunrise-cart-id";
    private static final String DEFAULT_MINI_CART_SESSION_KEY = "sunrise-mini-cart";
    private static final int LINE_ITEMS_LIMIT = 5;
    private final String cartIdSessionKey;
    private final String miniCartSessionKey;
    private final ObjectStoringSessionStrategy session;
    @Inject
    private Injector injector;

    @Inject
    public CartInSession(final ObjectStoringSessionStrategy session, final Configuration configuration) {
        this.cartIdSessionKey = configuration.getString("session.cart.cartId", DEFAULT_CART_ID_SESSION_KEY);
        this.miniCartSessionKey = configuration.getString("session.cart.miniCart", DEFAULT_MINI_CART_SESSION_KEY);
        this.session = session;
    }

    public Optional<String> findCartId() {
        return session.findValueByKey(cartIdSessionKey);
    }

    public Optional<MiniCartBean> findMiniCart() {
        return session.findObjectByKey(miniCartSessionKey, MiniCartBean.class);
    }

    @Override
    public void store(@Nullable final Cart value) {
        super.store(value);
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    protected void storeAssociatedData(final Cart cart) {
        session.overwriteObjectByKey(miniCartSessionKey, createMiniCart(cart));
        session.overwriteValueByKey(cartIdSessionKey, cart.getId());
    }

    @Override
    protected void removeAssociatedData() {
        session.removeObjectByKey(miniCartSessionKey);
        session.removeValueByKey(cartIdSessionKey);
    }

    protected MiniCartBean createMiniCart(final Cart cart) {
        return injector.getInstance(TruncatedMiniCartBeanFactory.class).create(cart, LINE_ITEMS_LIMIT);
    }
}
