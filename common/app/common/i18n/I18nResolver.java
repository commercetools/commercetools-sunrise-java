package common.i18n;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

/**
 * Resolves i18n messages.
 */
@FunctionalInterface
public interface I18nResolver {

    /**
     * Resolves i18n message identified by a bundle and a key for the the first found given locale.
     * @param locales the list of locales used to translate the message
     * @param bundle name of the bundle containing the message key
     * @param key identifier of the message
     * @param hashArgs list of hash arguments
     * @return the resolved message in the any of the given languages, or absent if it could not be found
     */
    Optional<String> get(final List<Locale> locales, final String bundle, final String key, final Map<String, Object> hashArgs);

    /**
     * Resolves i18n message identified by a bundle and a key for the the first found given locale.
     * @param locales the list of locales used to translate the message
     * @param bundle name of the bundle containing the message key
     * @param key identifier of the message
     * @return the resolved message in the given language, or absent if it could not be found
     */
    default Optional<String> get(final List<Locale> locales, final String bundle, final String key) {
        return get(locales, bundle, key, emptyMap());
    }

    /**
     * Resolves i18n message identified by a bundle and a key for the the first found given locale.
     * @param locales the list of locales used to translate the message
     * @param bundle name of the bundle containing the message key
     * @param key identifier of the message
     * @param hashArgs list of hash arguments
     * @return the resolved message in the first found given language, or empty string if it could not be found
     */
    default String getOrEmpty(final List<Locale> locales, final String bundle, final String key, final Map<String, Object> hashArgs) {
        return get(locales, bundle, key, hashArgs).orElse("");
    }

    /**
     * Resolves i18n message identified by a bundle and a key for the the first found given locale.
     * @param locales the list of locales used to translate the message
     * @param bundle name of the bundle containing the message key
     * @param key identifier of the message
     * @return the resolved message in the any of the given languages, or empty string if it could not be found
     */
    default String getOrEmpty(final List<Locale> locales, final String bundle, final String key) {
        return get(locales, bundle, key).orElse("");
    }
}
