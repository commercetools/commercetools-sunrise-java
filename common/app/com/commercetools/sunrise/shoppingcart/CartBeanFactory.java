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
public class CartBeanFactory extends CartLikeBeanFactory {

    private final LineItemExtendedBeanFactory lineItemExtendedBeanFactory;

    @Inject
    public CartBeanFactory(final UserContext userContext, final PriceFormatter priceFormatter,
                           final AddressBeanFactory addressBeanFactory,
                           final LineItemExtendedBeanFactory lineItemExtendedBeanFactory) {
        super(userContext, priceFormatter, addressBeanFactory);
        this.lineItemExtendedBeanFactory = lineItemExtendedBeanFactory;
    }

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
