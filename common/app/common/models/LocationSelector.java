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
    private List<SelectableBean> language;
    private List<SelectableBean> country;

    public LocationSelector() {
    }

    public LocationSelector(final ProjectContext projectContext, final UserContext userContext) {
        this.country = createCountry(projectContext.countries(), userContext.country());
        this.language = createLanguage(projectContext.locales(), userContext.locale());
    }

    public List<SelectableBean> getLanguage() {
        return language;
    }

    public void setLanguage(final List<SelectableBean> language) {
        this.language = language;
    }

    public List<SelectableBean> getCountry() {
        return country;
    }

    public void setCountry(final List<SelectableBean> country) {
        this.country = country;
    }

    private static List<SelectableBean> createCountry(final List<CountryCode> countryCodes, @Nullable final CountryCode selectedCountryCode) {
        final List<SelectableBean> countrySelector = countryCodes.stream()
                .map(countryCode -> {
                    final SelectableBean selector = new SelectableBean(countryCode.getName(), countryCode.getAlpha2());
                    if (countryCode.equals(selectedCountryCode)) {
                        selector.setSelected(true);
                    }
                    return selector;
                }).collect(toList());
        return (countrySelector.size() > 1) ? countrySelector : emptyList();
    }

    private static List<SelectableBean> createLanguage(final List<Locale> locales, @Nullable final Locale selectedLocale) {
        final List<SelectableBean> localeSelector = locales.stream()
                .map(locale -> {
                    final SelectableBean selector = new SelectableBean(locale.getDisplayName(), locale.getLanguage());
                    if (locale.equals(selectedLocale)) {
                        selector.setSelected(true);
                    }
                    return selector;
                }).collect(toList());
        return (localeSelector.size() > 1) ? localeSelector : emptyList();
    }
}
