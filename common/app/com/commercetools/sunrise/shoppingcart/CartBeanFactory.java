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
public class CartBeanFactory extends AbstractCartBeanFactory<CartBean, Cart> {

    private final LineItemExtendedBeanFactory lineItemExtendedBeanFactory;

    @Inject
    public CartBeanFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final ShippingInfoBeanFactory shippingInfoBeanFactory,
                           final PaymentInfoBeanFactory paymentInfoBeanFactory, final AddressBeanFactory addressBeanFactory,
                           final LineItemExtendedBeanFactory lineItemExtendedBeanFactory) {
        super(currency, priceFormatter, shippingInfoBeanFactory, paymentInfoBeanFactory, addressBeanFactory);
        this.lineItemExtendedBeanFactory = lineItemExtendedBeanFactory;
    }

    public final CartBean create(@Nullable final Cart cart) {
        final Data<Cart> data = new Data<>(cart);
        return initializedViewModel(data);
    }

    @Override
    protected CartBean getViewModelInstance() {
        return new CartBean();
    }

    @Override
    protected final void initialize(final CartBean bean, final Data<Cart> data) {
        super.initialize(bean, data);
    }

    @Override
    protected final LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemExtendedBeanFactory.create(lineItem);
    }
}
