package com.commercetools.sunrise.framework.viewmodels.content.carts;

import io.sphere.sdk.cartdiscounts.DiscountedLineItemPrice;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.TaxedPrice;
import io.sphere.sdk.shippingmethods.ShippingRate;

import javax.money.MonetaryAmount;
import java.util.Optional;

import static io.sphere.sdk.products.PriceUtils.zeroAmount;

public final class CartPriceUtils {

    private CartPriceUtils() {
    }

    public static MonetaryAmount calculateTotalPrice(final CartLike<?> cartLike) {
        return Optional.ofNullable(cartLike.getTaxedPrice())
                .map(TaxedPrice::getTotalGross)
                .orElseGet(cartLike::getTotalPrice);
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

    private static MonetaryAmount calculateCartPriceWithoutShipping(final Cart cart) {
        return Optional.ofNullable(cart.getShippingInfo())
                .map(shippingInfo -> cart.getTotalPrice().subtract(shippingInfo.getPrice()))
                .orElseGet(cart::getTotalPrice);
    }
}
