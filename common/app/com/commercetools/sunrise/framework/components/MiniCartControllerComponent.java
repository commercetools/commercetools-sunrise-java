package com.commercetools.sunrise.framework.components;

import com.commercetools.sunrise.common.models.carts.MiniCartBean;
import com.commercetools.sunrise.common.models.carts.MiniCartBeanFactory;
import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.sessions.cart.CartInSession;

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
