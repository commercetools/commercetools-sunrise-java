package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.sessions.SessionHandlerBase;
import com.google.inject.Injector;
import io.sphere.sdk.carts.Cart;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.Optional;

import static java.util.Arrays.asList;

public class CartSessionHandler extends SessionHandlerBase<Cart> {

    private static final String CART_ID_SESSION_KEY = "sunrise-cart-id";
    private static final String MINI_CART_SESSION_KEY = "sunrise-mini-cart";

    @Inject
    private Injector injector;

    public Optional<String> findCartId(final Http.Session session) {
        return findValueByKey(session, getCartIdSessionKey());
    }

    public Optional<MiniCartBean> findMiniCart(final Http.Session session) {
        return findValueByKey(session, getMiniCartSessionKey(), MiniCartBean.class);
    }

    @Override
    protected void overwriteRelatedValuesInSession(final Http.Session session, final Cart cart) {
        overwriteStringValueByKey(session, getCartIdSessionKey(), cart.getId());
        overwriteValueByKey(session, getMiniCartSessionKey(), injector.getInstance(MiniCartBeanFactory.class).create(cart));
    }

    @Override
    protected void removeRelatedValuesFromSession(final Http.Session session) {
        removeValuesByKey(session, asList(getCartIdSessionKey(), getMiniCartSessionKey()));
    }

    protected String getCartIdSessionKey() {
        return CART_ID_SESSION_KEY;
    }

    protected String getMiniCartSessionKey() {
        return MINI_CART_SESSION_KEY;
    }
}
