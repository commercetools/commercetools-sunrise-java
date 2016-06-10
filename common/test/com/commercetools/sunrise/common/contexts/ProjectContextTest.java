package com.commercetools.sunrise.common.contexts;

import com.neovisionaries.i18n.CountryCode;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static java.util.Locale.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ProjectContextTest {
    private static final List<Locale> AVAILABLE_LANGUAGES = asList(ENGLISH, CHINESE, GERMAN);
    private static final List<CountryCode> AVAILABLE_COUNTRIES = asList(CountryCode.UK, CountryCode.CN, CountryCode.DE);
    private static final CurrencyUnit GBP = Monetary.getCurrency("GBP");
    private static final CurrencyUnit CNY = Monetary.getCurrency("CNY");
    private static final CurrencyUnit EUR = Monetary.getCurrency("EUR");
    private static final CurrencyUnit USD = Monetary.getCurrency("USD");
    private static final List<CurrencyUnit> AVAILABLE_CURRENCIES = asList(GBP, CNY, EUR);
    private static final ProjectContext PROJECT_CONTEXT = ProjectContext.of(AVAILABLE_LANGUAGES, AVAILABLE_COUNTRIES, AVAILABLE_CURRENCIES);

    @Test
    public void worksWithLocales() throws Exception {
        assertThat(PROJECT_CONTEXT.locales()).containsExactlyElementsOf(AVAILABLE_LANGUAGES);
        assertThat(PROJECT_CONTEXT.defaultLocale()).isEqualTo(ENGLISH);
        assertThat(PROJECT_CONTEXT.isLocaleAccepted(GERMAN)).isTrue();
        assertThat(PROJECT_CONTEXT.isLocaleAccepted(FRENCH)).isFalse();
    }

    @Test
    public void worksWithCountries() throws Exception {
        assertThat(PROJECT_CONTEXT.countries()).containsExactlyElementsOf(AVAILABLE_COUNTRIES);
        assertThat(PROJECT_CONTEXT.defaultCountry()).isEqualTo(CountryCode.UK);
        assertThat(PROJECT_CONTEXT.isCountryAccepted(CountryCode.DE)).isTrue();
        assertThat(PROJECT_CONTEXT.isCountryAccepted(CountryCode.FR)).isFalse();
    }

    @Test
    public void worksWithCurrencies() throws Exception {
        assertThat(PROJECT_CONTEXT.currencies()).containsExactlyElementsOf(AVAILABLE_CURRENCIES);
        assertThat(PROJECT_CONTEXT.defaultCurrency()).isEqualTo(GBP);
        assertThat(PROJECT_CONTEXT.isCurrencyAccepted(EUR)).isTrue();
        assertThat(PROJECT_CONTEXT.isCurrencyAccepted(USD)).isFalse();
    }
}
