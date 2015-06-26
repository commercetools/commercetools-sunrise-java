package common.contexts;

import com.neovisionaries.i18n.CountryCode;

import java.util.List;
import java.util.Locale;

public class ProjectContext {
    private final List<Locale> languages;
    private final List<CountryCode> countries;

    public ProjectContext(final List<Locale> languages, final List<CountryCode> countries) {
        this.languages = languages;
        this.countries = countries;
    }

    public List<Locale> getLanguages() {
        return languages;
    }

    public List<CountryCode> getCountries() {
        return countries;
    }
}
