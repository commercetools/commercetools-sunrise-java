package com.commercetools.sunrise.framework.localization;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.google.inject.AbstractModule;
import com.neovisionaries.i18n.CountryCode;

import javax.money.CurrencyUnit;
import java.util.Locale;

/**
 * Module that allows to inject locale, country and currency corresponding to the current user.
 */
public final class LocalizationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Locale.class)
                .toProvider(LocaleFromUrlProvider.class)
                .in(RequestScoped.class);

        bind(CountryCode.class)
                .toProvider(CountryFromSessionProvider.class)
                .in(RequestScoped.class);

        bind(CurrencyUnit.class)
                .toProvider(CurrencyFromCountryProvider.class)
                .in(RequestScoped.class);
    }
}
