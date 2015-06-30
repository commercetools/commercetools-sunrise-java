package common.contexts;

import com.neovisionaries.i18n.CountryCode;

import java.util.Locale;

/**
 * A container for all information related to the current user, such as selected country or language.
 */
public class UserContext {
    private final Locale language;
    private final CountryCode country;

    private UserContext(final Locale language, final CountryCode country) {
        this.language = language;
        this.country = country;
    }

    public Locale language() {
        return language;
    }

    public CountryCode country() {
        return country;
    }

    public static UserContext of(final Locale language, final CountryCode country) {
        return new UserContext(language, country);
    }
}
