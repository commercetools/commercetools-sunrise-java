package common.utils;

import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

final class PriceFormatterImpl extends Base implements PriceFormatter {

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
}
