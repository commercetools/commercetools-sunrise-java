package com.commercetools.sunrise.common.models;

import com.neovisionaries.i18n.CountryCode;
import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.contexts.UserContext;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class LocationSelector extends Base {

    private List<FormSelectableOptionBean> language;
    private List<FormSelectableOptionBean> country;

    public LocationSelector() {
    }

    public LocationSelector(final ProjectContext projectContext, final UserContext userContext) {
        this.country = createCountry(projectContext.countries(), userContext.country());
        this.language = createLanguage(projectContext.locales(), userContext.locale());
    }

    public List<FormSelectableOptionBean> getLanguage() {
        return language;
    }

    public void setLanguage(final List<FormSelectableOptionBean> language) {
        this.language = language;
    }

    public List<FormSelectableOptionBean> getCountry() {
        return country;
    }

    public void setCountry(final List<FormSelectableOptionBean> country) {
        this.country = country;
    }

    private static List<FormSelectableOptionBean> createCountry(final List<CountryCode> countryCodes, @Nullable final CountryCode selectedCountryCode) {
        final List<FormSelectableOptionBean> countrySelector = countryCodes.stream()
                .map(countryCode -> {
                    final FormSelectableOptionBean selector = new FormSelectableOptionBean(countryCode.getName(), countryCode.getAlpha2());
                    if (countryCode.equals(selectedCountryCode)) {
                        selector.setSelected(true);
                    }
                    return selector;
                }).collect(toList());
        return (countrySelector.size() > 1) ? countrySelector : emptyList();
    }

    private static List<FormSelectableOptionBean> createLanguage(final List<Locale> locales, @Nullable final Locale selectedLocale) {
        final List<FormSelectableOptionBean> localeSelector = locales.stream()
                .map(locale -> {
                    final FormSelectableOptionBean selector = new FormSelectableOptionBean(locale.getDisplayName(), locale.getLanguage());
                    if (locale.equals(selectedLocale)) {
                        selector.setSelected(true);
                    }
                    return selector;
                }).collect(toList());
        return (localeSelector.size() > 1) ? localeSelector : emptyList();
    }
}
