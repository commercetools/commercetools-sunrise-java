package com.commercetools.sunrise.framework.components;

import com.commercetools.sunrise.common.models.carts.MiniCartViewModel;
import com.commercetools.sunrise.common.models.carts.MiniCartViewModelFactory;
import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.sessions.cart.CartInSession;

import javax.inject.Inject;

public class MiniCartControllerComponent implements ControllerComponent, PageDataReadyHook {

    private final CartInSession cartInSession;
    private final MiniCartViewModelFactory miniCartViewModelFactory;

    @Inject
    public MiniCartControllerComponent(final CartInSession cartInSession, final MiniCartViewModelFactory miniCartViewModelFactory) {
        this.cartInSession = cartInSession;
        this.miniCartViewModelFactory = miniCartViewModelFactory;
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        final MiniCartViewModel miniCart = cartInSession.findMiniCart()
                .orElseGet(() -> miniCartViewModelFactory.create(null));
        pageData.getHeader().setMiniCart(miniCart);
    }
}
