package com.commercetools.sunrise.common.utils;

import com.commercetools.sunrise.common.contexts.UserContext;
import io.sphere.sdk.cartdiscounts.DiscountedLineItemPrice;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.carts.TaxedPrice;
import io.sphere.sdk.productdiscounts.DiscountedPrice;
import io.sphere.sdk.products.PriceLike;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.shippingmethods.ShippingRate;
import io.sphere.sdk.utils.MoneyImpl;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Optional;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public final class PriceUtils {

    private PriceUtils() {
    }

    public static MonetaryAmount calculateTotalPrice(final CartLike<?> cartLike) {
        return Optional.ofNullable(cartLike.getTaxedPrice())
                .map(TaxedPrice::getTotalGross)
                .orElseGet(cartLike::getTotalPrice);
    }

    public static Optional<MonetaryAmount> calculateSalesTax(final CartLike<?> cartLike) {
        return Optional.ofNullable(cartLike.getTaxedPrice())
                .map(taxedPrice -> taxedPrice.getTotalGross().subtract(taxedPrice.getTotalNet()));
    }

    public static MonetaryAmount calculateSubTotal(final CartLike<?> cartLike) {
        final MonetaryAmount zeroAmount = zeroAmount(cartLike.getCurrency());
        return cartLike.getLineItems().stream()
                .map(LineItem::getTotalPrice)
                .reduce(zeroAmount, MonetaryAmount::add);
    }

    public static MonetaryAmount zeroAmount(final CurrencyUnit currency) {
        return MoneyImpl.ofCents(0, currency);
    }

    public static Optional<MonetaryAmount> findAppliedShippingPrice(final CartLike<?> cartLike) {
        return Optional.ofNullable(cartLike.getShippingInfo())
                .map(shippingInfo -> Optional.ofNullable(shippingInfo.getDiscountedPrice())
                        .map(DiscountedLineItemPrice::getValue)
                        .orElseGet(shippingInfo::getPrice));
    }

    public static Optional<MonetaryAmount> findPreviousShippingPrice(final CartLike<?> cartLike) {
        return Optional.ofNullable(cartLike.getShippingInfo())
                .flatMap(shippingInfo -> Optional.ofNullable(shippingInfo.getDiscountedPrice())
                        .map(discountedPrice -> shippingInfo.getPrice()));
    }

    public static MonetaryAmount calculateShippingPrice(final Cart cart, final ShippingRate shippingRate) {
        return Optional.ofNullable(shippingRate.getFreeAbove())
                .filter(freeAbove -> calculateCartPriceWithoutShipping(cart).isGreaterThan(freeAbove))
                .map(freeAbove -> zeroAmount(cart.getCurrency()))
                .orElseGet(shippingRate::getPrice);
    }

    public static MonetaryAmount calculateCartPriceWithoutShipping(final Cart cart) {
        return Optional.ofNullable(cart.getShippingInfo())
                .map(shippingInfo -> cart.getTotalPrice().subtract(shippingInfo.getPrice()))
                .orElseGet(cart::getTotalPrice);
    }

    public static Optional<MonetaryAmount> findAppliedProductPrice(final ProductVariant variant) {
        return findSelectedPrice(variant)
                .map(PriceUtils::findAppliedPrice);
    }

    public static MonetaryAmount findAppliedProductPrice(final LineItem lineItem) {
        return findAppliedPrice(lineItem.getPrice());
    }

//    public static Optional<MonetaryAmount> findDiscountedPrice(final ProductVariant variant) {

    public static Optional<MonetaryAmount> findPreviousProductPrice(final ProductVariant variant) {
         return findSelectedPrice(variant)
                .flatMap(PriceUtils::findPreviousPrice);
    }

    public static Optional<MonetaryAmount> findPreviousProductPrice(final LineItem lineItem) {
        return findPreviousPrice(lineItem.getPrice());
    }

    //    }
//                .flatMap(PriceUtils::findPriceWithDiscount);
//        return findSelectedPrice(variant)
    private static MonetaryAmount findAppliedPrice(final PriceLike price) {
        return findPriceWithDiscount(price)
                .orElseGet(price::getValue);
    }

    private static Optional<MonetaryAmount> findPreviousPrice(final PriceLike price) {
        return findPriceWithDiscount(price)
                .map(priceWithDiscount -> price.getValue());
    }

    private static Optional<MonetaryAmount> findPriceWithDiscount(final PriceLike price) {
        return Optional.ofNullable(price.getDiscounted())
                .map(DiscountedPrice::getValue);
    }

    private static Optional<PriceLike> findSelectedPrice(final ProductVariant variant) {
        final PriceLike price = firstNonNull(variant.getPrice(), variant.getScopedPrice());
        return Optional.ofNullable(price);
    }

    public static PriceSelection createPriceSelection(final UserContext userContext) {
        return PriceSelection.of(userContext.currency())
                .withPriceCountry(userContext.country())
                .withPriceCustomerGroup(userContext.customerGroup().orElse(null))
                .withPriceChannel(userContext.channel().orElse(null));
    }
}
