package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;

@RequestScoped
public class MiniCartViewModelFactory extends AbstractMiniCartViewModelFactory<MiniCartViewModel, Cart> {

    private final LineItemViewModelFactory lineItemViewModelFactory;

    @Inject
    public MiniCartViewModelFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final LineItemViewModelFactory lineItemViewModelFactory) {
        super(currency, priceFormatter);
        this.lineItemViewModelFactory = lineItemViewModelFactory;
    }

    @Override
    protected MiniCartViewModel getViewModelInstance() {
        return new MiniCartViewModel();
    }

    @Override
    public final MiniCartViewModel create(@Nullable final Cart cart) {
        return super.create(cart);
    }

    @Override
    protected final void initialize(final MiniCartViewModel viewModel, final Cart cart) {
        super.initialize(viewModel, cart);
    }

    @Override
    LineItemViewModel createLineItem(final LineItem lineItem) {
        return lineItemViewModelFactory.create(lineItem);
    }
}