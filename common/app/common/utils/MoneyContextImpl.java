package common.utils;

import io.sphere.sdk.products.Price;
import io.sphere.sdk.products.PriceLike;
import io.sphere.sdk.utils.MoneyImpl;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Locale;
import java.util.Optional;

final class MoneyContextImpl implements MoneyContext {
    private final CurrencyUnit currency;
    private final Locale locale;

    public MoneyContextImpl(final CurrencyUnit currency, final Locale locale) {
        this.currency = currency;
        this.locale = locale;
    }

    @Override
    public MonetaryAmount zero() {
        return MoneyImpl.of(0, currency);
    }

    private String format(final MonetaryAmount monetaryAmount) {
        return PriceFormatter.of(locale, currency).format(monetaryAmount);
    }

    @Override
    public String formatOrZero(@Nullable final MonetaryAmount monetaryAmount) {
        final MonetaryAmount concreteAmount = Optional.ofNullable(monetaryAmount).orElseGet(this::zero);
        return format(concreteAmount);
    }

    @Override
    public String formatOrNull(@Nullable final PriceLike price) {
        final MonetaryAmount concreteAmount = Optional.ofNullable(price)
                .map(PriceLike::getValue)
                .orElseGet(this::zero);
        return formatOrNull(concreteAmount);
    }

    @Override
    public String formatOrNull(@Nullable final MonetaryAmount monetaryAmount) {
        return Optional.ofNullable(monetaryAmount)
                .map(this::format)
                .orElse(null);
    }
}
