package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.sessions.SessionHandlerBase;
import io.sphere.sdk.carts.Cart;
import play.Configuration;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

import static java.util.Arrays.asList;

@Singleton
public class CartSessionHandler extends SessionHandlerBase<Cart> {

    private static final String DEFAULT_CART_ID_SESSION_KEY = "sunrise-cart-id";
    private static final String DEFAULT_MINI_CART_SESSION_KEY = "sunrise-mini-cart";
    private final String cartIdSessionKey;
    private final String miniCartSessionKey;
    @Inject
    private MiniCartBeanFactory miniCartBeanFactory;

    @Inject
    public CartSessionHandler(final Configuration configuration) {
        this.cartIdSessionKey = configuration.getString("session.cart.cartId", DEFAULT_CART_ID_SESSION_KEY);
        this.miniCartSessionKey = configuration.getString("session.cart.miniCart", DEFAULT_MINI_CART_SESSION_KEY);
    }

    public Optional<String> findCartId(final Http.Session session) {
        return findValueByKey(session, cartIdSessionKey);
    }

    public Optional<MiniCartBean> findMiniCart(final Http.Session session) {
        return findValueByKey(session, miniCartSessionKey, MiniCartBean.class);
    }

    @Override
    protected void overwriteRelatedValuesInSession(final Http.Session session, final Cart cart) {
        overwriteStringValueByKey(session, cartIdSessionKey, cart.getId());
        overwriteValueByKey(session, miniCartSessionKey, miniCartBeanFactory.create(cart));
    }

    @Override
    protected void removeRelatedValuesFromSession(final Http.Session session) {
        removeValuesByKey(session, asList(cartIdSessionKey, miniCartSessionKey));
    }
}
