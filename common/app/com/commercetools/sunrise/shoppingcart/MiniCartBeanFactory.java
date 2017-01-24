package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;

@RequestScoped
public class MiniCartBeanFactory extends AbstractMiniCartBeanFactory<MiniCartBean, Cart> {

    private final LineItemBeanFactory lineItemBeanFactory;

    @Inject
    public MiniCartBeanFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final LineItemBeanFactory lineItemBeanFactory) {
        super(currency, priceFormatter);
        this.lineItemBeanFactory = lineItemBeanFactory;
    }

    public final MiniCartBean create(@Nullable final Cart cart) {
        final Data<Cart> data = new Data<>(cart);
        return initializedViewModel(data);
    }

    @Override
    protected final void initialize(final MiniCartBean bean, final Data<Cart> data) {
        super.initialize(bean, data);
    }

    @Override
    protected MiniCartBean getViewModelInstance() {
        return new MiniCartBean();
    }

    @Override
    protected final LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemBeanFactory.create(lineItem);
    }
}