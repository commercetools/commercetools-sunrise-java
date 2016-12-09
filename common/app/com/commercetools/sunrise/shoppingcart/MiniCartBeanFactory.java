package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class MiniCartBeanFactory extends CartLikeBeanFactory {

    private final LineItemBeanFactory lineItemBeanFactory;

    @Inject
    public MiniCartBeanFactory(final UserContext userContext, final PriceFormatter priceFormatter,
                               final AddressBeanFactory addressBeanFactory, final LineItemBeanFactory lineItemBeanFactory) {
        super(userContext, priceFormatter, addressBeanFactory);
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
            fillEmptyMiniCartInfo(bean);
        }
    }

    @Override
    protected LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemBeanFactory.create(lineItem);
    }
}
