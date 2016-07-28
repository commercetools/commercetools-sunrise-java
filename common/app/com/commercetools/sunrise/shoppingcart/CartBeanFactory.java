package com.commercetools.sunrise.shoppingcart;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;

import javax.inject.Inject;

public class CartBeanFactory extends MiniCartBeanFactory {

    @Inject
    private LineItemExtendedBeanFactory lineItemExtendedBeanFactory;

    public CartBean create(final Cart cart) {
        final CartBean bean = new CartBean();
        initialize(bean, cart);
        return bean;
    }

    protected final void initialize(final CartBean bean, final Cart cart) {
        super.initialize(bean, cart);
        fillSalesTax(bean, cart);
        fillSubtotalPrice(bean, cart);
        fillCustomerEmail(bean, cart);
        fillShippingAddress(bean, cart);
        fillBillingAddress(bean, cart);
        fillShippingMethod(bean, cart);
        fillPaymentDetails(bean, cart);
    }

    @Override
    protected LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemExtendedBeanFactory.create(lineItem);
    }
}
