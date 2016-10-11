package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class MiniCartBeanFactory extends CartLikeBeanFactory {

    @Inject
    protected UserContext userContext;
    @Inject
    protected ProductDataConfig productDataConfig;
    @Inject
    private LineItemBeanFactory lineItemBeanFactory;

    public MiniCartBean create(@Nullable final Cart cart) {
        final MiniCartBean bean = new MiniCartBean();
        initialize(bean, cart);
        return bean;
    }

    protected final void initialize(final MiniCartBean bean, @Nullable final Cart cart) {
        if (cart != null) {
            fillMiniCartInfo(bean, cart);
        } else {
            fillEmptyMiniCartInfo(bean);
        }
    }

    @Override
    protected LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemBeanFactory.create(lineItem);
    }
}
