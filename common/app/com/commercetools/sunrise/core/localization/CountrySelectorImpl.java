package com.commercetools.sunrise.core.localization;

import com.commercetools.sunrise.core.viewmodels.ViewModelFactory;
import com.commercetools.sunrise.models.SelectOption;
import com.neovisionaries.i18n.CountryCode;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

@Singleton
final class CountrySelectorImpl extends ViewModelFactory implements CountrySelector {

    private final Countries countries;
    private final Provider<CountryCode> countryProvider;
    private final Provider<Locale> localeProvider;

    @Inject
    CountrySelectorImpl(final Countries countries, final Provider<CountryCode> countryProvider, final Provider<Locale> localeProvider) {
        this.countries = countries;
        this.countryProvider = countryProvider;
        this.localeProvider = localeProvider;
    }

    @Override
    public List<SelectOption> options() {
        final CountryCode selectedCountry = countryProvider.get();
        final Locale locale = localeProvider.get();
        return countries.availables().stream()
                .map(country -> createOption(country, selectedCountry, locale))
                .collect(toList());
    }

    private SelectOption createOption(final CountryCode country, final CountryCode selectedCountry, final Locale locale) {
        final SelectOption option = new SelectOption();
        option.setLabel(country.toLocale().getDisplayCountry(locale));
        option.setValue(country.getAlpha2());
        option.setSelected(country.equals(selectedCountry));
        return option;
    }
}
