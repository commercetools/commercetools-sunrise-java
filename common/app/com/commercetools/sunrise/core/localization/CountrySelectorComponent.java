package com.commercetools.sunrise.core.localization;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.SelectOption;
import com.neovisionaries.i18n.CountryCode;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

@Singleton
public final class CountrySelectorComponent implements ControllerComponent, PageDataHook {

    private final Provider<CountryCode> countryProvider;
    private final Provider<Locale> localeProvider;
    private final Countries countries;

    @Inject
    CountrySelectorComponent(final Provider<CountryCode> countryProvider, final Provider<Locale> localeProvider, final Countries countries) {
        this.countryProvider = countryProvider;
        this.localeProvider = localeProvider;
        this.countries = countries;
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return completedFuture(pageData.put("countryOptions", options()));
    }

    private List<SelectOption> options() {
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
