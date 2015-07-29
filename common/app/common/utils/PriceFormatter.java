package common.utils;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

public class PriceFormatter {

    private final Locale locale;

    private PriceFormatter(final Locale locale) {
        this.locale = locale;
    }

    public static PriceFormatter of(final Locale locale) {
        return new PriceFormatter(locale);
    }

    public String format(MonetaryAmount price) {
        final MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(locale);
        return format.format(price);
    }
}
