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
     * @return the resolved message in the given language
     */
    Optional<String> get(final String bundle, final String key, final Locale locale);

    /**
     * Resolves i18n message identified by a bundle and a key for the the first found given locale.
     * @param bundle name of the bundle containing the message key
     * @param key identifier of the message
     * @param locales the list of locales used to translate the message
     * @return the resolved message in the first found given language
     */
    default Optional<String> get(final String bundle, final String key, final List<Locale> locales) {
        for (final Locale locale : locales) {
            final Optional<String> message = get(bundle, key, locale);
            if (message.isPresent()) {
                return message;
            }
        }
        return Optional.empty();
    }

    default String getOrEmpty(final String bundle, final String key, final Locale locale) {
        return get(bundle, key, locale).orElse("");
    }

    default String getOrEmpty(final String bundle, final String key, final List<Locale> locales) {
        return get(bundle, key, locales).orElse("");
    }

    static I18nResolver of(final String filepath, final List<Locale> locales, final List<String> bundles) {
        return new YamlI18nResolver(filepath, locales, bundles);
    }
}
