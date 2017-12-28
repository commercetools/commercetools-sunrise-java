package com.commercetools.sunrise.core.localization;

import com.neovisionaries.i18n.CountryCode;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Currency;

/**
 * Provides the {@link CurrencyUnit} corresponding to the injected {@link CountryCode}.
 */
@Singleton
public final class CurrencyFromCountryProvider implements Provider<CurrencyUnit> {

    private final Provider<CountryCode> countryCodeProvider;

    @Inject
    CurrencyFromCountryProvider(final Provider<CountryCode> countryCodeProvider) {
        this.countryCodeProvider = countryCodeProvider;
    }

    @Override
    public CurrencyUnit get() {
        final Currency currency = countryCodeProvider.get().getCurrency();
        return Monetary.getCurrency(currency.getCurrencyCode());
    }
}
