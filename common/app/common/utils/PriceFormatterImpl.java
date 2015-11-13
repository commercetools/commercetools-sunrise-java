package common.utils;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.utils.MoneyImpl;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.format.MonetaryFormats;
import java.util.Locale;
import java.util.Optional;

final class PriceFormatterImpl extends Base implements ZeroPriceFormatter {

    private final Locale locale;
    @Nullable
    private final CurrencyUnit currencyUnit;

    PriceFormatterImpl(final Locale locale) {
        this(locale, null);
    }

    PriceFormatterImpl(final Locale locale, final CurrencyUnit currencyUnit) {
        this.locale = locale;
        this.currencyUnit = currencyUnit;
    }

    @Override
    public String format(MonetaryAmount monetaryAmount) {
        return MonetaryFormats.getAmountFormat(locale).format(monetaryAmount);
    }

    @Override
    public String formatOrEmpty(@Nullable final MonetaryAmount monetaryAmount) {
        return Optional.ofNullable(monetaryAmount).map(this::format).orElse("");
    }

    @Override
    public String formatOrZero(@Nullable final MonetaryAmount monetaryAmount) {
        return Optional.ofNullable(monetaryAmount)
                .map(this::format)
                .orElseGet(() -> format(MoneyImpl.of(0, currencyUnit)));
    }
}
