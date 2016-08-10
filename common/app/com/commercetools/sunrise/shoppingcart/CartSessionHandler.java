package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.sessions.SessionHandlerBase;
import com.google.inject.Injector;
import io.sphere.sdk.carts.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;

public class CartSessionHandler extends SessionHandlerBase<Cart> {

    private static final Logger logger = LoggerFactory.getLogger(CartSessionHandler.class);
    private static final String CART_ID_SESSION_KEY = "sunrise-cart-id";
    private static final String MINI_CART_SESSION_KEY = "sunrise-mini-cart";

    @Inject
    private Injector injector;

    public Optional<String> findCartId(final Http.Session session) {
        return findValue(session, getCartIdSessionKey());
    }

    public Optional<MiniCartBean> findMiniCart(final Http.Session session) {
        return findValue(session, getMiniCartSessionKey(), MiniCartBean.class);
    }

    @Override
    protected void overwriteRelatedValuesInSession(final Http.Session session, final Cart cart) {
        overwriteValue(session, getCartIdSessionKey(), cart.getId());
        overwriteValue(session, getMiniCartSessionKey(), injector.getInstance(MiniCartBeanFactory.class).create(cart));
    }

    @Override
    protected Set<String> sessionKeys() {
        return new HashSet<>(asList(getCartIdSessionKey(), getMiniCartSessionKey()));
    }

    @Override
    protected Logger logger() {
        return logger;
    }

    protected String getCartIdSessionKey() {
        return CART_ID_SESSION_KEY;
    }

    protected String getMiniCartSessionKey() {
        return MINI_CART_SESSION_KEY;
    }
}
