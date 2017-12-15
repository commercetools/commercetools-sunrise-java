package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.controllers.ControllerComponent;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.core.hooks.application.PageDataReadyHook;

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
