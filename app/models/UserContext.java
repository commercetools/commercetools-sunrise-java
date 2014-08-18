package models;

import com.neovisionaries.i18n.CountryCode;
import play.i18n.Lang;

import java.util.Locale;

/**
 * A container for all information related to the current user, such as country, language, cart, associated customer.
 */
public class UserContext {
    private final Lang lang;
    private final CountryCode countryCode;

    private UserContext(final Lang lang, final CountryCode countryCode) {
        this.lang = lang;
        this.countryCode = countryCode;
    }

    public static UserContext of(final Lang lang, final CountryCode countryCode) {
        return new UserContext(lang, countryCode);
    }

    public Lang lang() {
        return lang;
    }

    public Locale locale() {
        return lang.toLocale();
    }

    public CountryCode country() {
        return countryCode;
    }
}
