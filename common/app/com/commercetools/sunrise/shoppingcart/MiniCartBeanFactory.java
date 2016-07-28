package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;

import javax.inject.Inject;

public class MiniCartBeanFactory extends CartLikeBeanFactory {

    @Inject
    protected UserContext userContext;
    @Inject
    protected ProductDataConfig productDataConfig;
    @Inject
    private LineItemBeanFactory lineItemBeanFactory;

    public MiniCartBean createWithEmptyCart() {
        final MiniCartBean bean = new MiniCartBean();
        initializeWithEmptyCart(bean);
        return bean;
    }

    public MiniCartBean create(final Cart cart) {
        final MiniCartBean bean = new MiniCartBean();
        initialize(bean, cart);
        return bean;
    }

    protected final void initialize(final MiniCartBean bean, final Cart cart) {
        fillTotalPrice(bean, cart);
        fillTotalItems(bean, cart);
        fillLineItems(bean, cart);
    }

    protected final void initializeWithEmptyCart(final MiniCartBean bean) {
        bean.setTotalItems(0L);
    }

    @Override
    protected LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemBeanFactory.create(lineItem);
    }
}
