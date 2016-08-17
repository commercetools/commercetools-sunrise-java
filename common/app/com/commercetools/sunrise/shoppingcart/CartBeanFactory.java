package com.commercetools.sunrise.shoppingcart;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class CartBeanFactory extends CartLikeBeanFactory {

    @Inject
    private LineItemExtendedBeanFactory lineItemExtendedBeanFactory;

    public CartBean create(@Nullable final Cart cart) {
        final CartBean bean = new CartBean();
        initialize(bean, cart);
        return bean;
    }

    protected final void initialize(final CartBean bean, @Nullable final Cart cart) {
        if (cart != null) {
            fillCartInfo(bean, cart);
        } else {
            fillEmptyCartInfo(bean);
        }
    }

    @Override
    protected LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemExtendedBeanFactory.create(lineItem);
    }

}
