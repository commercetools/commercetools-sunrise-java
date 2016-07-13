package com.commercetools.sunrise.common.contexts;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Base;

import javax.money.CurrencyUnit;
import java.util.List;
import java.util.Locale;

public final class ProjectContextImpl extends Base implements ProjectContext {

    private final List<Locale> locales;
    private final List<CountryCode> countryCodes;
    private final List<CurrencyUnit> currencies;

    private ProjectContextImpl(final List<Locale> locales, final List<CountryCode> countryCodes, final List<CurrencyUnit> currencies) {
        this.locales = locales;
        this.countryCodes = countryCodes;
        this.currencies = currencies;
    }

    @Override
    public List<Locale> locales() {
        return locales;
    }

    @Override
    public List<CountryCode> countries() {
        return countryCodes;
    }

    @Override
    public List<CurrencyUnit> currencies() {
        return currencies;
    }

    public static ProjectContext of(final List<Locale> locales, final List<CountryCode> countries, final List<CurrencyUnit> currencies) {
        return new ProjectContextImpl(locales, countries, currencies);
    }
}
