package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.sessions.cart.CartInSession;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;

import javax.inject.Inject;

public class MiniCartControllerComponent implements ControllerComponent, PageDataReadyHook {

    private final CartInSession cartInSession;
    private final MiniCartBeanFactory miniCartBeanFactory;

    @Inject
    public MiniCartControllerComponent(final CartInSession cartInSession, final MiniCartBeanFactory miniCartBeanFactory) {
        this.cartInSession = cartInSession;
        this.miniCartBeanFactory = miniCartBeanFactory;
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        final MiniCartBean miniCart = cartInSession.findMiniCart()
                .orElseGet(() -> miniCartBeanFactory.create(null));
        pageData.getHeader().setMiniCart(miniCart);
    }
}
