package com.commercetools.sunrise.framework.viewmodels.formatters;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import org.javamoney.moneta.format.CurrencyStyle;

import javax.inject.Inject;
import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQuery;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

import static org.javamoney.moneta.format.AmountFormatParams.PATTERN;

@RequestScoped
final class PriceFormatterImpl implements PriceFormatter {

    private static final String PATTERN_WITHOUT_DECIMAL = "¤ #,###,##0.-";
    private static final String PATTERN_WITH_DECIMAL = "¤ #,###,##0.00";
    private final Locale locale;

    @Inject
    PriceFormatterImpl(final Locale locale) {
        this.locale = locale;
    }

    @Override
    public String format(final MonetaryAmount monetaryAmount) {
        final boolean isDecimal = monetaryAmount.getNumber().doubleValueExact() % 1 != 0;//TODO this can be improved with monetary query
        final AmountFormatQuery pattern = AmountFormatQueryBuilder.of(locale)
                .set(CurrencyStyle.SYMBOL)
                .set(PATTERN, isDecimal ? PATTERN_WITH_DECIMAL : PATTERN_WITHOUT_DECIMAL)
                .build();
        return MonetaryFormats.getAmountFormat(pattern).format(monetaryAmount);
    }
}
