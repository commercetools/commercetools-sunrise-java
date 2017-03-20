package com.commercetools.sunrise.framework.localization;

import com.neovisionaries.i18n.CountryCode;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Optional;

public final class CurrencyFromCountryProvider implements Provider<CurrencyUnit> {

    private final CountryCode country;
    private final ProjectContext projectContext;

    @Inject
    public CurrencyFromCountryProvider(final CountryCode country, final ProjectContext projectContext) {
        this.country = country;
        this.projectContext = projectContext;
    }

    @Override
    public CurrencyUnit get() {
        return findCurrentCurrency()
                .filter(projectContext::isCurrencySupported)
                .orElseGet(projectContext::defaultCurrency);
    }

    private Optional<CurrencyUnit> findCurrentCurrency() {
        return Optional.ofNullable(country.getCurrency())
                .map(countryCurrency -> Monetary.getCurrency(countryCurrency.getCurrencyCode()));
    }
}
