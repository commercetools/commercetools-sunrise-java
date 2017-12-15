package com.commercetools.sunrise.core.localization;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.google.inject.AbstractModule;
import com.neovisionaries.i18n.CountryCode;

import javax.money.CurrencyUnit;

/**
 * Module that allows to inject country and currency corresponding to the current user.
 */
public final class LocalizationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CountryCode.class)
                .toProvider(CountryFromSessionProvider.class)
                .in(RequestScoped.class);

        bind(CurrencyUnit.class)
                .toProvider(CurrencyFromCountryProvider.class)
                .in(RequestScoped.class);
    }
}
