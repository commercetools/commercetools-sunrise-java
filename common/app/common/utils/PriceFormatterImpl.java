package common.utils;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

public class PriceFormatterImpl implements PriceFormatter {

    private final Locale locale;

    PriceFormatterImpl(final Locale locale) {
        this.locale = locale;
    }

    @Override
    public String format(MonetaryAmount price) {
        return MonetaryFormats.getAmountFormat(locale).format(price);
    }
}
