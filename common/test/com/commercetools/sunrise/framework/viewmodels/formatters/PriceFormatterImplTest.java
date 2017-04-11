package com.commercetools.sunrise.framework.viewmodels.formatters;

import org.javamoney.moneta.Money;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static java.util.Locale.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PriceFormatterImplTest {

    private static final String EUR = "EUR";
    private static final String USD = "USD";
    private static final String GBP = "GBP";
    private static final String JPY = "JPY";

    @Test
    public void numberWithoutDecimal() throws Exception {
        final String formattedPrice = priceFormatter(US).format(money(49, USD));
        assertThat(formattedPrice).isEqualTo("$ 49.-");
    }

    @Test
    public void numberWithDecimals() throws Exception {
        final String formattedPrice = priceFormatter(GERMANY).format(money(79.99, EUR));
        assertThat(formattedPrice).isEqualTo("€ 79,99");
    }

    @Test
    public void numberWithOneDigit() throws Exception {
        final String formattedPrice = priceFormatter(UK).format(money(7.90, GBP));
        assertThat(formattedPrice).isEqualTo("£ 7.90");
    }

    @Test
    public void numberWithManyDigits() throws Exception {
        final String formattedPrice = priceFormatter(JAPAN).format(money(76543210.90, JPY));
        assertThat(formattedPrice).isEqualTo("￥ 76,543,210.90");
    }

    @Test
    public void localeWithoutCountry() throws Exception {
        final String formattedPrice = priceFormatter(ENGLISH).format(money(49, USD));
        assertThat(formattedPrice).isEqualTo("USD 49.-");
    }

    private Money money(final double value, final String currency) {
        return Money.of(BigDecimal.valueOf(value), currency);
    }

    private PriceFormatterImpl priceFormatter(final Locale locale) {
        return new PriceFormatterImpl(locale);
    }
}
