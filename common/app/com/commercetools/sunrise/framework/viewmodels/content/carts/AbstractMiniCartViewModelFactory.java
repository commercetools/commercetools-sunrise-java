package com.commercetools.sunrise.framework.viewmodels.content.carts;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.formatters.PriceFormatter;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.products.PriceUtils;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.stream.Collectors;

import static com.commercetools.sunrise.framework.viewmodels.content.carts.CartPriceUtils.calculateTotalPrice;

public abstract class AbstractMiniCartViewModelFactory<M extends MiniCartViewModel, I extends CartLike<?>> extends SimpleViewModelFactory<M, I> {

    private final CurrencyUnit currency;
    private final PriceFormatter priceFormatter;

    protected AbstractMiniCartViewModelFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter) {
        this.currency = currency;
        this.priceFormatter = priceFormatter;
    }

    protected final CurrencyUnit getCurrency() {
        return currency;
    }

    protected final PriceFormatter getPriceFormatter() {
        return priceFormatter;
    }

    @Override
    protected void initialize(final M viewModel, @Nullable final I cartLike) {
        fillTotalPrice(viewModel, cartLike);
        fillTotalItems(viewModel, cartLike);
        fillLineItems(viewModel, cartLike);
    }

    protected void fillTotalItems(final M viewModel, @Nullable final I cartLike) {
        final long totalItems;
        if (cartLike != null) {
            totalItems = cartLike.getLineItems().stream()
                    .mapToLong(LineItem::getQuantity)
                    .sum();
        } else {
            totalItems = 0;
        }
        viewModel.setTotalItems(totalItems);
    }

    protected void fillLineItems(final M viewModel, @Nullable final I cartLike) {
        if (cartLike != null) {
            viewModel.setLineItems(createLineItemList(cartLike));
        }
    }

    protected void fillTotalPrice(final M viewModel, @Nullable final I cartLike) {
        final MonetaryAmount totalPrice;
        if (cartLike != null) {
            totalPrice = calculateTotalPrice(cartLike);
        } else {
            totalPrice = zeroAmount(currency);
        }
        viewModel.setTotalPrice(priceFormatter.format(totalPrice));
    }

    private LineItemListViewModel createLineItemList(final CartLike<?> cartLike) {
        final LineItemListViewModel lineItemListViewModel = new LineItemListViewModel();
        lineItemListViewModel.setList(cartLike.getLineItems().stream()
                .map(this::createLineItem)
                .collect(Collectors.toList()));
        return lineItemListViewModel;
    }

    abstract LineItemViewModel createLineItem(final LineItem lineItem);

    protected final MonetaryAmount zeroAmount(final CurrencyUnit currency) {
        return PriceUtils.zeroAmount(currency);
    }
}
