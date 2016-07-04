package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.hooks.PageDataHook;
import com.commercetools.sunrise.framework.ControllerComponent;
import play.mvc.Http;

import javax.inject.Inject;

public class MiniCartControllerComponent implements ControllerComponent, PageDataHook {
    @Inject
    private Http.Context context;

    @Override
    public void acceptPageData(final PageData pageData) {
        pageData.getHeader()
                .setMiniCart(CartSessionUtils.getMiniCart(context.session()));
    }
}
