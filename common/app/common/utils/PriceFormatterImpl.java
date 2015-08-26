package common.utils;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

public class PriceFormatterImpl implements PriceFormatter {

    private final Locale locale;

    private PriceFormatterImpl(final Locale locale) {
        this.locale = locale;
    }

    public static PriceFormatter of(final Locale locale) {
        return new PriceFormatterImpl(locale);
    }

    @Override
    public String format(MonetaryAmount price) {
        return MonetaryFormats.getAmountFormat(locale).format(price);
    }
}
