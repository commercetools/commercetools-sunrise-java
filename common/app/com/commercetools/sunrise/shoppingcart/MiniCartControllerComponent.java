package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;

import javax.inject.Inject;

public class MiniCartControllerComponent implements ControllerComponent, PageDataReadyHook {

    @Inject
    private CartInSession cartInSession;
    @Inject
    private MiniCartBeanFactory miniCartBeanFactory;

    @Override
    public void onPageDataReady(final PageData pageData) {
        final MiniCartBean miniCart = cartInSession.findMiniCart()
                .orElseGet(() -> miniCartBeanFactory.create(null));
        pageData.getHeader()
                .setMiniCart(miniCart);
    }
}
