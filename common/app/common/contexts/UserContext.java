package common.contexts;

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

    public Lang lang() {
        return lang;
    }

    public CountryCode country() {
        return countryCode;
    }

    /**
     * Gets the locale representing the language and country of the user.
     * @return the locale matching the language and country.
     */
    public Locale locale() {
        if (lang.country().isEmpty()) {
            return new Locale(lang.language(), countryCode.getAlpha2());
        } else {
            return lang.toLocale();
        }
    }

    public static UserContext of(final Lang lang, final CountryCode countryCode) {
        return new UserContext(lang, countryCode);
    }
}
