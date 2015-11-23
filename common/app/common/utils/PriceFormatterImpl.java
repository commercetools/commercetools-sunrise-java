package common.utils;

import org.javamoney.moneta.format.CurrencyStyle;

import javax.annotation.Nullable;
import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQuery;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryFormats;
import java.util.Locale;
import java.util.Optional;

import static org.javamoney.moneta.format.AmountFormatParams.PATTERN;

public class PriceFormatterImpl implements PriceFormatter {
    private static final String PATTERN_WITHOUT_DECIMAL = "¤ #,###,##0.-";
    private static final String PATTERN_WITH_DECIMAL = "¤ #,###,##0.00";
    private final Locale locale;

    PriceFormatterImpl(final Locale locale) {
        this.locale = locale;
    }

    @Override
    public String format(@Nullable final MonetaryAmount price) {
        return Optional.ofNullable(price).map(p -> {
            final boolean isDecimal = p.getNumber().doubleValueExact() % 1 != 0;//TODO this can be improved with monetary query
            final AmountFormatQuery pattern = AmountFormatQueryBuilder.of(locale)
                    .set(CurrencyStyle.SYMBOL)
                    .set(PATTERN, isDecimal ? PATTERN_WITH_DECIMAL : PATTERN_WITHOUT_DECIMAL)
                    .build();
            return MonetaryFormats.getAmountFormat(pattern).format(p);
        }).orElse("");
    }
}
