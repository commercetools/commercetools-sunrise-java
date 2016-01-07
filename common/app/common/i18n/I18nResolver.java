package common.i18n;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Resolves i18n messages.
 */
@FunctionalInterface
public interface I18nResolver {

    /**
     * Resolves i18n message identified by a bundle and a key for the given locale.
     * @param bundle name of the bundle containing the message key
     * @param key identifier of the message
     * @param locale the locale used to translate the message
     * @param args optional list of arguments
     * @return the resolved message in the given language, or absent if it could not be found
     */
    Optional<String> get(final String bundle, final String key, final Locale locale, final Object... args);

    /**
     * Resolves i18n message identified by a bundle and a key for the the first found given locale.
     * @param bundle name of the bundle containing the message key
     * @param key identifier of the message
     * @param locales the list of locales used to translate the message
     * @param args optional list of arguments
     * @return the resolved message in the any of the given languages, or absent if it could not be found
     */
    default Optional<String> get(final String bundle, final String key, final List<Locale> locales, final Object... args) {
        for (final Locale locale : locales) {
            final Optional<String> message = get(bundle, key, locale, args);
            if (message.isPresent()) {
                return message;
            }
        }
        return Optional.empty();
    }

    /**
     * Resolves i18n message identified by a bundle and a key for the the first found given locale.
     * @param bundle name of the bundle containing the message key
     * @param key identifier of the message
     * @param locale the locale used to translate the message
     * @param args optional list of arguments
     * @return the resolved message in the first found given language, or empty string if it could not be found
     */
    default String getOrEmpty(final String bundle, final String key, final Locale locale, final Object... args) {
        return get(bundle, key, locale, args).orElse("");
    }

    /**
     * Resolves i18n message identified by a bundle and a key for the the first found given locale.
     * @param bundle name of the bundle containing the message key
     * @param key identifier of the message
     * @param locales the list of locales used to translate the message
     * @param args optional list of arguments
     * @return the resolved message in the any of the given languages, or empty string if it could not be found
     */
    default String getOrEmpty(final String bundle, final String key, final List<Locale> locales, final Object... args) {
        return get(bundle, key, locales, args).orElse("");
    }
}
