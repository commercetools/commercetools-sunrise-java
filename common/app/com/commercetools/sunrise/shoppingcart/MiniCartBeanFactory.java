package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class MiniCartBeanFactory extends CartLikeBeanFactory {

    private final LineItemBeanFactory lineItemBeanFactory;

    @Inject
    public MiniCartBeanFactory(final PriceFormatter priceFormatter, final LineItemBeanFactory lineItemBeanFactory) {
        super(priceFormatter);
        this.lineItemBeanFactory = lineItemBeanFactory;
    }

    public MiniCartBean create(@Nullable final Cart cart) {
        final MiniCartBean bean = new MiniCartBean();
        initialize(bean, cart);
        return bean;
    }

    protected final void initialize(final MiniCartBean bean, @Nullable final Cart cart) {
        if (cart != null) {
            fillMiniCartInfo(bean, cart);
        } else {
            bean.setTotalItems(0L);
        }
    }

    @Override
    protected LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemBeanFactory.create(lineItem);
    }
}
