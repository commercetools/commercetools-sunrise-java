package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.PageDataHook;
import play.mvc.Http;

import javax.inject.Inject;

public class MiniCartControllerComponent implements ControllerComponent, PageDataHook {

    @Inject
    private Http.Context context;
    @Inject
    private CartSessionHandler cartSessionHandler;
    @Inject
    private MiniCartBeanFactory miniCartBeanFactory;

    @Override
    public void acceptPageData(final PageData pageData) {
        final MiniCartBean miniCart = cartSessionHandler.findMiniCart(context.session())
                .orElseGet(() -> miniCartBeanFactory.createWithEmptyCart());
        pageData.getHeader().setMiniCart(miniCart);
    }
}
