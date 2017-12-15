package com.commercetools.sunrise.framework.localization;

import com.neovisionaries.i18n.CountryCode;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Provides the {@link CurrencyUnit} corresponding to the injected {@link CountryCode}.
 */
public final class CurrencyFromCountryProvider implements Provider<CurrencyUnit> {

    private final Currencies currencies;
    private final CountryCode country;

    @Inject
    CurrencyFromCountryProvider(final Currencies currencies, final CountryCode country) {
        this.currencies = currencies;
        this.country = country;
    }

    @Override
    public CurrencyUnit get() {
        return currencies.preferred(candidates());
    }

    private List<CurrencyUnit> candidates() {
        final String currencyCode = country.getCurrency().getCurrencyCode();
        return singletonList(Monetary.getCurrency(currencyCode));
    }
}
