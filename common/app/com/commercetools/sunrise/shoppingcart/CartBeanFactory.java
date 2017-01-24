package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;

@RequestScoped
public class CartBeanFactory extends AbstractCartLikeBeanFactory<CartBean, CartBeanFactory.Data, Cart> {

    private final LineItemExtendedBeanFactory lineItemExtendedBeanFactory;

    @Inject
    public CartBeanFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final ShippingInfoBeanFactory shippingInfoBeanFactory,
                           final PaymentInfoBeanFactory paymentInfoBeanFactory, final AddressBeanFactory addressBeanFactory,
                           final LineItemExtendedBeanFactory lineItemExtendedBeanFactory) {
        super(currency, priceFormatter, shippingInfoBeanFactory, paymentInfoBeanFactory, addressBeanFactory);
        this.lineItemExtendedBeanFactory = lineItemExtendedBeanFactory;
    }

    public final CartBean create(@Nullable final Cart cart) {
        final Data data = new Data(cart);
        return initializedViewModel(data);
    }

    @Override
    protected CartBean getViewModelInstance() {
        return new CartBean();
    }

    @Override
    protected final void initialize(final CartBean bean, final Data data) {
        super.initialize(bean, data);
    }

    @Override
    LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemExtendedBeanFactory.create(lineItem);
    }

    protected final static class Data extends AbstractMiniCartBeanFactory.Data<Cart> {

        @Nullable
        public final Cart cart;

        public Data(@Nullable final Cart cart) {
            super(cart);
            this.cart = cart;
        }
    }
}
