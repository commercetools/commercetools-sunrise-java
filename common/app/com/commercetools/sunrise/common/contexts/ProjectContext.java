package com.commercetools.sunrise.common.contexts;

import com.neovisionaries.i18n.CountryCode;

import javax.money.CurrencyUnit;
import java.util.List;
import java.util.Locale;

public class ProjectContext {
    private final List<Locale> locales;
    private final List<CountryCode> countryCodes;
    private final List<CurrencyUnit> currencies;

    private ProjectContext(final List<Locale> locales, final List<CountryCode> countryCodes, final List<CurrencyUnit> currencies) {
        this.locales = locales;
        this.countryCodes = countryCodes;
        this.currencies = currencies;
    }

    public List<Locale> locales() {
        return locales;
    }

    public List<CountryCode> countries() {
        return countryCodes;
    }

    public List<CurrencyUnit> currencies() {
        return currencies;
    }

    public Locale defaultLocale() {
        return locales.stream().findFirst().get();
    }

    public CountryCode defaultCountry() {
        return countryCodes.stream().findFirst().get();
    }

    public CurrencyUnit defaultCurrency() {
        return currencies.stream().findFirst().get();
    }

    public boolean isLocaleAccepted(final Locale locale) {
        return locales.contains(locale);
    }

    public boolean isCountryAccepted(final CountryCode countryCode) {
        return countryCodes.contains(countryCode);
    }

    public boolean isCurrencyAccepted(final CurrencyUnit currency) {
        return currencies.contains(currency);
    }

    public static ProjectContext of(final List<Locale> locales, final List<CountryCode> countries, final List<CurrencyUnit> currencies) {
        return new ProjectContext(locales, countries, currencies);
    }
}
