package common.utils;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Locale;

public interface PriceFormatter {

    String format(MonetaryAmount monetaryAmount);

    static PriceFormatter of(final Locale locale) {
        return new PriceFormatterImpl(locale);
    }

    static PriceFormatter of(final Locale locale, final CurrencyUnit currencyUnit) {
        return new PriceFormatterImpl(locale);
    }
}
