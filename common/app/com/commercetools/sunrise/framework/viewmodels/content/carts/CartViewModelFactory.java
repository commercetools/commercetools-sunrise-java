package com.commercetools.sunrise.framework.viewmodels.content.carts;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.content.addresses.AddressViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.formatters.PriceFormatter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;

@RequestScoped
public class CartViewModelFactory extends AbstractCartLikeViewModelFactory<CartViewModel, Cart> {

    private final LineItemExtendedViewModelFactory lineItemExtendedViewModelFactory;

    @Inject
    public CartViewModelFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final ShippingInfoViewModelFactory shippingInfoViewModelFactory,
                                final PaymentInfoViewModelFactory paymentInfoViewModelFactory, final AddressViewModelFactory addressViewModelFactory,
                                final LineItemExtendedViewModelFactory lineItemExtendedViewModelFactory,
                                final DiscountCodeViewModelFactory discountCodeViewModelFactory) {
        super(currency, priceFormatter, shippingInfoViewModelFactory, paymentInfoViewModelFactory, addressViewModelFactory, discountCodeViewModelFactory);
        this.lineItemExtendedViewModelFactory = lineItemExtendedViewModelFactory;
    }

    protected final LineItemExtendedViewModelFactory getLineItemExtendedViewModelFactory() {
        return lineItemExtendedViewModelFactory;
    }

    @Override
    protected CartViewModel newViewModelInstance(@Nullable final Cart cart) {
        return new CartViewModel();
    }

    @Override
    public final CartViewModel create(@Nullable final Cart cart) {
        return super.create(cart);
    }

    @Override
    protected final void initialize(final CartViewModel viewModel, @Nullable final Cart cart) {
        super.initialize(viewModel, cart);
    }

    @Override
    LineItemViewModel createLineItem(final LineItem lineItem) {
        return lineItemExtendedViewModelFactory.create(lineItem);
    }
}
