package com.commercetools.sunrise.shoppingcart;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;

import javax.inject.Inject;

public class CartBeanFactory extends CartLikeBeanFactory {

    @Inject
    private LineItemExtendedBeanFactory lineItemExtendedBeanFactory;

    public CartBean create(final Cart cart) {
        final CartBean bean = new CartBean();
        initialize(bean, cart);
        return bean;
    }

    protected final void initialize(final CartBean bean, final Cart cart) {
        fillCartInfo(bean, cart);
    }

    @Override
    protected LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemExtendedBeanFactory.create(lineItem);
    }
}
