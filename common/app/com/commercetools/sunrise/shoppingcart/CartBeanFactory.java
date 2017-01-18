package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;

@RequestScoped
public class CartBeanFactory extends CartLikeBeanFactory {

    private final CurrencyUnit currency;
    private final LocalizedStringResolver localizedStringResolver;
    private final AddressBeanFactory addressBeanFactory;
    private final LineItemExtendedBeanFactory lineItemExtendedBeanFactory;

    @Inject
    public CartBeanFactory(final PriceFormatter priceFormatter, final CurrencyUnit currency, final LocalizedStringResolver localizedStringResolver,
                           final AddressBeanFactory addressBeanFactory, final LineItemExtendedBeanFactory lineItemExtendedBeanFactory) {
        super(priceFormatter);
        this.currency = currency;
        this.localizedStringResolver = localizedStringResolver;
        this.addressBeanFactory = addressBeanFactory;
        this.lineItemExtendedBeanFactory = lineItemExtendedBeanFactory;
    }

    public CartBean create(@Nullable final Cart cart) {
        final CartBean bean = new CartBean();
        initialize(bean, cart);
        return bean;
    }

    protected final void initialize(final CartBean bean, @Nullable final Cart cart) {
        if (cart != null) {
            fillCartInfo(bean, cart, localizedStringResolver, addressBeanFactory);
        } else {
            fillEmptyCartInfo(bean, currency);
        }
    }

    @Override
    protected LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemExtendedBeanFactory.create(lineItem);
    }
}
