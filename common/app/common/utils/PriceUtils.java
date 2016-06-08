package common.utils;

import common.contexts.UserContext;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.TaxedPrice;
import io.sphere.sdk.products.PriceLike;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.utils.MoneyImpl;

import javax.money.MonetaryAmount;
import java.util.Optional;

public final class PriceUtils {

    private PriceUtils() {
    }

    public static MonetaryAmount calculateTotalPrice(final CartLike<?> cartLike) {
        final Optional<TaxedPrice> taxedPriceOpt = Optional.ofNullable(cartLike.getTaxedPrice());
        return taxedPriceOpt.map(TaxedPrice::getTotalGross).orElseGet(cartLike::getTotalPrice);
    }

    public static Optional<MonetaryAmount> calculateSalesTax(final CartLike<?> cartLike) {
        return Optional.ofNullable(cartLike.getTaxedPrice())
                .map(taxedPrice -> taxedPrice.getTotalGross().subtract(taxedPrice.getTotalNet()));
    }

    public static MonetaryAmount calculateSubTotal(final CartLike<?> cartLike) {
        final MonetaryAmount zeroAmount = MoneyImpl.ofCents(0, cartLike.getCurrency());
        return cartLike.getLineItems().stream()
                .map(lineItem -> {
                    final MonetaryAmount basePrice = calculateFinalPrice(lineItem.getPrice());
                    return basePrice.multiply(lineItem.getQuantity());
                }).reduce(zeroAmount, (left, right) -> left.add(right));
    }

    public static MonetaryAmount calculateFinalPrice(final PriceLike price) {
        final boolean hasProductDiscount = price.getDiscounted() != null;
        return hasProductDiscount ? price.getDiscounted().getValue() : price.getValue();
    }

    public static PriceSelection createPriceSelection(final UserContext userContext) {
        return PriceSelection.of(userContext.currency())
                .withPriceCountry(userContext.country())
                .withPriceCustomerGroup(userContext.customerGroup().orElse(null))
                .withPriceChannel(userContext.channel().orElse(null));
    }
}
