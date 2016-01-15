package common.models;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.ProjectContext;
import common.contexts.UserContext;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class LocationSelector extends Base {
    private List<SelectableData> language;
    private List<SelectableData> country;

    public LocationSelector() {
    }

    public LocationSelector(final ProjectContext projectContext, final UserContext userContext) {
        this.country = createCountry(projectContext.countries(), userContext.country());
        this.language = createLanguage(projectContext.locales(), userContext.locale());
    }

    public List<SelectableData> getLanguage() {
        return language;
    }

    public void setLanguage(final List<SelectableData> language) {
        this.language = language;
    }

    public List<SelectableData> getCountry() {
        return country;
    }

    public void setCountry(final List<SelectableData> country) {
        this.country = country;
    }

    private static List<SelectableData> createCountry(final List<CountryCode> countryCodes, @Nullable final CountryCode selectedCountryCode) {
        final List<SelectableData> countrySelector = countryCodes.stream()
                .map(countryCode -> {
                    final SelectableData selector = new SelectableData(countryCode.getName(), countryCode.getAlpha2());
                    if (countryCode.equals(selectedCountryCode)) {
                        selector.setSelected(true);
                    }
                    return selector;
                }).collect(toList());
        return (countrySelector.size() > 1) ? countrySelector : emptyList();
    }

    private static List<SelectableData> createLanguage(final List<Locale> locales, @Nullable final Locale selectedLocale) {
        final List<SelectableData> localeSelector = locales.stream()
                .map(locale -> {
                    final SelectableData selector = new SelectableData(locale.getDisplayName(), locale.getLanguage());
                    if (locale.equals(selectedLocale)) {
                        selector.setSelected(true);
                    }
                    return selector;
                }).collect(toList());
        return (localeSelector.size() > 1) ? localeSelector : emptyList();
    }
}
