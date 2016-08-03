package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.utils.MoneyContext;
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

    public CartBean createWithEmptyCart(){
        final CartBean bean = new CartBean();
        initializeWithEmptyCart(bean);
        return bean;
    }

    protected final void initialize(final CartBean bean, final Cart cart) {
        fillCartInfo(bean, cart);
    }

    protected final void initializeWithEmptyCart(final CartBean bean) {
        bean.setTotalItems(0L);
        final MoneyContext moneyContext = MoneyContext.of(userContext.currency(), userContext.locale());
        bean.setSalesTax(moneyContext.formatOrZero(null));
        bean.setTotalPrice(moneyContext.formatOrZero(null));
        bean.setSubtotalPrice(moneyContext.formatOrZero(null));
    }

    @Override
    protected LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemExtendedBeanFactory.create(lineItem);
    }

}
