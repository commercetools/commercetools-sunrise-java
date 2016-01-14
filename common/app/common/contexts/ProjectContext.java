package common.contexts;

import com.neovisionaries.i18n.CountryCode;

import java.util.List;
import java.util.Locale;

import static io.sphere.sdk.utils.IterableUtils.requireNonEmpty;

public class ProjectContext {
    private final List<Locale> locales;
    private final List<CountryCode> countryCodes;

    private ProjectContext(final List<Locale> locales, final List<CountryCode> countryCodes) {
        requireNonEmpty(locales);
        requireNonEmpty(countryCodes);
        this.locales = locales;
        this.countryCodes = countryCodes;
    }

    public List<Locale> locales() {
        return locales;
    }

    public List<CountryCode> countries() {
        return countryCodes;
    }

    public Locale defaultLocale() {
        return locales.stream().findFirst().get();
    }

    public CountryCode defaultCountry() {
        return countryCodes.stream().findFirst().get();
    }

    public boolean isLocaleAccepted(final Locale locale) {
        return locales.contains(locale);
    }

    public boolean isCountryAccepted(final CountryCode countryCode) {
        return countryCodes.contains(countryCode);
    }

    public static ProjectContext of(final List<Locale> locales, final List<CountryCode> countries) {
        return new ProjectContext(locales, countries);
    }
}
