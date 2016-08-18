package com.commercetools.sunrise.common.contexts;

import com.google.inject.ImplementedBy;
import com.neovisionaries.i18n.CountryCode;

import javax.money.CurrencyUnit;
import java.util.List;
import java.util.Locale;

/**
 * A container for all information related to the project, such as supported countries, languages or currencies.
 */
@ImplementedBy(ProjectContextImpl.class)
public interface ProjectContext {

    /**
     * Locales associated to the project.
     * @return the list of locales
     */
    List<Locale> locales();

    /**
     * Countries associated to the project.
     * @return the list of country codes
     */
    List<CountryCode> countries();

    /**
     * Currencies associated to the project.
     * @return the list of currency units
     */
    List<CurrencyUnit> currencies();

    default Locale defaultLocale() {
        return locales().stream()
                .filter(locale -> locale != null)
                .findFirst()
                .orElseThrow(() -> new NoLocaleFoundException("Project does not have any valid locale associated"));
    }

    default CountryCode defaultCountry() {
        return countries().stream()
                .filter(country -> country != null)
                .findFirst()
                .orElseThrow(() -> new NoCountryFoundException("Project does not have any valid country code associated"));
    }

    default CurrencyUnit defaultCurrency() {
        return currencies().stream()
                .filter(country -> country != null)
                .findFirst()
                .orElseThrow(() -> new NoCurrencyFoundException("Project does not have any valid currency unit associated"));
    }

    default boolean isLocaleSupported(final Locale locale) {
        return locales().contains(locale);
    }

    default boolean isCountrySupported(final CountryCode countryCode) {
        return countries().contains(countryCode);
    }

    default boolean isCurrencySupported(final CurrencyUnit currency) {
        return currencies().contains(currency);
    }
}
