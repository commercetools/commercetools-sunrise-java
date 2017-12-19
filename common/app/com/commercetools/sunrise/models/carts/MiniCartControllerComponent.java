package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.controllers.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.core.viewmodels.PageData;

import javax.inject.Inject;

public class MiniCartControllerComponent implements ControllerComponent, PageDataReadyHook {

    private final CartInSession cartInSession;

    @Inject
    public MiniCartControllerComponent(final CartInSession cartInSession) {
        this.cartInSession = cartInSession;
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        pageData.getHeader().setMiniCart(cartInSession.findMiniCart().orElse(null));
    }
}
