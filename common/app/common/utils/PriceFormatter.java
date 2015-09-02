package common.utils;

import javax.money.MonetaryAmount;
import java.util.Locale;

@FunctionalInterface
public interface PriceFormatter {

    String format(MonetaryAmount price);

    static PriceFormatter of(final Locale locale) {
        return new PriceFormatterImpl(locale);
    }
}
