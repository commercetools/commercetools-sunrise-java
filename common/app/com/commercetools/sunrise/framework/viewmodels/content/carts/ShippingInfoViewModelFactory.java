package com.commercetools.sunrise.framework.viewmodels.content.carts;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.formatters.PriceFormatter;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.PriceUtils;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

@RequestScoped
public class ShippingInfoViewModelFactory extends SimpleViewModelFactory<ShippingInfoViewModel, CartLike<?>> {

    private final CurrencyUnit currency;
    private final PriceFormatter priceFormatter;

    @Inject
    public ShippingInfoViewModelFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter) {
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
    protected ShippingInfoViewModel newViewModelInstance(@Nullable final CartLike<?> cartLike) {
        return new ShippingInfoViewModel();
    }

    @Override
    public final ShippingInfoViewModel create(@Nullable final CartLike<?> cartLike) {
        return super.create(cartLike);
    }

    @Override
    protected final void initialize(final ShippingInfoViewModel viewModel, @Nullable final CartLike<?> cartLike) {
        fillLabel(viewModel, cartLike);
        fillDescription(viewModel, cartLike);
        fillPrice(viewModel, cartLike);
    }

    protected void fillLabel(final ShippingInfoViewModel viewModel, @Nullable final CartLike<?> cartLike) {
        if (cartLike != null && cartLike.getShippingInfo() != null) {
            viewModel.setLabel(cartLike.getShippingInfo().getShippingMethodName());
        }
    }

    protected void fillDescription(final ShippingInfoViewModel viewModel, @Nullable final CartLike<?> cartLike) {
        if (cartLike != null && cartLike.getShippingInfo() != null) {
            final Reference<ShippingMethod> ref = cartLike.getShippingInfo().getShippingMethod();
            if (ref != null && ref.getObj() != null) {
                viewModel.setDescription(ref.getObj().getDescription());
            }
        }
    }

    protected void fillPrice(final ShippingInfoViewModel viewModel, @Nullable final CartLike<?> cartLike) {
        if (cartLike != null) {
            final MonetaryAmount amount = CartPriceUtils.calculateAppliedShippingPrice(cartLike)
                    .orElseGet(() -> zeroAmount(cartLike.getCurrency()));
            viewModel.setPrice(priceFormatter.format(amount));
        } else {
            viewModel.setPrice(priceFormatter.format(zeroAmount(currency)));
        }
    }

    private static MonetaryAmount zeroAmount(final CurrencyUnit currency) {
        return PriceUtils.zeroAmount(currency);
    }
}
