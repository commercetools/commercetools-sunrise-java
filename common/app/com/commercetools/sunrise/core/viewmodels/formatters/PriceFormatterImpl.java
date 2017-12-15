package com.commercetools.sunrise.core.viewmodels.formatters;

import org.javamoney.moneta.format.CurrencyStyle;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQuery;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

import static org.javamoney.moneta.format.AmountFormatParams.PATTERN;

@Singleton
final class PriceFormatterImpl implements PriceFormatter {

    private static final String PATTERN_WITHOUT_DECIMAL = "¤ #,###,##0.-";
    private static final String PATTERN_WITH_DECIMAL = "¤ #,###,##0.00";

    private final Provider<Locale> localeProvider;

    @Inject
    PriceFormatterImpl(final Provider<Locale> localeProvider) {
        this.localeProvider = localeProvider;
    }

    @Override
    public String format(final MonetaryAmount monetaryAmount) {
        final boolean isDecimal = monetaryAmount.getNumber().doubleValueExact() % 1 != 0;//TODO this can be improved with monetary query
        final AmountFormatQuery pattern = AmountFormatQueryBuilder.of(localeProvider.get())
                .set(CurrencyStyle.SYMBOL)
                .set(PATTERN, isDecimal ? PATTERN_WITH_DECIMAL : PATTERN_WITHOUT_DECIMAL)
                .build();
        return MonetaryFormats.getAmountFormat(pattern).format(monetaryAmount);
    }
}
