package com.commercetools.sunrise.common.utils;

import com.commercetools.sunrise.common.contexts.UserContext;
import io.sphere.sdk.cartdiscounts.DiscountedLineItemPrice;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.carts.TaxedPrice;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.shippingmethods.ShippingRate;
import io.sphere.sdk.utils.MoneyImpl;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Optional;

public final class CartPriceUtils {

    private CartPriceUtils() {
    }

    public static MonetaryAmount zeroAmount(final CurrencyUnit currency) {
        return MoneyImpl.ofCents(0, currency);
    }

    public static MonetaryAmount calculateTotalPrice(final CartLike<?> cartLike) {
        return Optional.ofNullable(cartLike.getTaxedPrice())
                .map(TaxedPrice::getTotalGross)
                .orElseGet(cartLike::getTotalPrice);
    }

    public static Optional<MonetaryAmount> calculateAppliedTaxes(final CartLike<?> cartLike) {
        return Optional.ofNullable(cartLike.getTaxedPrice())
                .map(taxedPrice -> taxedPrice.getTotalGross().subtract(taxedPrice.getTotalNet()));
    }

    public static MonetaryAmount calculateSubTotalPrice(final CartLike<?> cartLike) {
        final MonetaryAmount zeroAmount = zeroAmount(cartLike.getCurrency());
        return cartLike.getLineItems().stream()
                .map(LineItem::getTotalPrice)
                .reduce(zeroAmount, MonetaryAmount::add);
    }

    public static Optional<MonetaryAmount> calculateAppliedShippingPrice(final CartLike<?> cartLike) {
        return Optional.ofNullable(cartLike.getShippingInfo())
                .map(shippingInfo -> Optional.ofNullable(shippingInfo.getDiscountedPrice())
                        .map(DiscountedLineItemPrice::getValue)
                        .orElseGet(shippingInfo::getPrice));
    }

    public static Optional<MonetaryAmount> calculatePreviousShippingPrice(final CartLike<?> cartLike) {
        return Optional.ofNullable(cartLike.getShippingInfo())
                .flatMap(shippingInfo -> Optional.ofNullable(shippingInfo.getDiscountedPrice())
                        .map(discountedPrice -> shippingInfo.getPrice()));
    }

    public static MonetaryAmount calculateApplicableShippingCosts(final Cart cart, final ShippingRate shippingRate) {
        return Optional.ofNullable(shippingRate.getFreeAbove())
                .filter(freeAbove -> calculateCartPriceWithoutShipping(cart).isGreaterThan(freeAbove))
                .map(freeAbove -> zeroAmount(cart.getCurrency()))
                .orElseGet(shippingRate::getPrice);
    }

    public static PriceSelection createPriceSelection(final UserContext userContext) {
        return PriceSelection.of(userContext.currency())
                .withPriceCountry(userContext.country())
                .withPriceCustomerGroup(userContext.customerGroup().orElse(null))
                .withPriceChannel(userContext.channel().orElse(null));
    }

    private static MonetaryAmount calculateCartPriceWithoutShipping(final Cart cart) {
        return Optional.ofNullable(cart.getShippingInfo())
                .map(shippingInfo -> cart.getTotalPrice().subtract(shippingInfo.getPrice()))
                .orElseGet(cart::getTotalPrice);
    }
}
