package com.commercetools.sunrise.common.template.i18n;

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
     * Resolves i18n message identified by a bundle and a key for the first found given locale.
     * @param locales the list of locales used to translate the message
     * @param i18nIdentifier identifier of the i18n message
     * @param hashArgs list of hash arguments
     * @return the resolved message in the first found given language, or absent if it could not be found
     */
    Optional<String> get(final List<Locale> locales, final I18nIdentifier i18nIdentifier, final Map<String, Object> hashArgs);

    /**
     * Resolves i18n message identified by a bundle and a key for the first found given locale.
     * @param locales the list of locales used to translate the message
     * @param i18nIdentifier identifier of the i18n message
     * @return the resolved message in the first found given language, or absent if it could not be found
     */
    default Optional<String> get(final List<Locale> locales, final I18nIdentifier i18nIdentifier) {
        return get(locales, i18nIdentifier, emptyMap());
    }

    /**
     * Resolves i18n message identified by a bundle and a key for the first found given locale.
     * @param locales the list of locales used to translate the message
     * @param i18nIdentifier identifier of the i18n message
     * @param hashArgs list of hash arguments
     * @return the resolved message in the first found given language, or empty string if it could not be found
     */
    default String getOrEmpty(final List<Locale> locales, final I18nIdentifier i18nIdentifier, final Map<String, Object> hashArgs) {
        return get(locales, i18nIdentifier, hashArgs).orElse("");
    }

    /**
     * Resolves i18n message identified by a bundle and a key for the first found given locale.
     * @param locales the list of locales used to translate the message
     * @param i18nIdentifier identifier of the i18n message
     * @return the resolved message in the any of the given languages, or empty string if it could not be found
     */
    default String getOrEmpty(final List<Locale> locales, final I18nIdentifier i18nIdentifier) {
        return get(locales, i18nIdentifier).orElse("");
    }

    /**
     * Resolves i18n message identified by a bundle and a key for the first found given locale.
     * @param locales the list of locales used to translate the message
     * @param i18nIdentifier identifier of the i18n message
     * @param hashArgs list of hash arguments
     * @return the resolved message in the first found given language, or the message key if it could not be found
     */
    default String getOrKey(final List<Locale> locales, final I18nIdentifier i18nIdentifier, final Map<String, Object> hashArgs) {
        return get(locales, i18nIdentifier, hashArgs).orElse(i18nIdentifier.getMessageKey());
    }

    /**
     * Resolves i18n message identified by a bundle and a key for the first found given locale.
     * @param locales the list of locales used to translate the message
     * @param i18nIdentifier identifier of the i18n message
     * @return the resolved message in the any of the given languages, or the message key if it could not be found
     */
    default String getOrKey(final List<Locale> locales, final I18nIdentifier i18nIdentifier) {
        return get(locales, i18nIdentifier).orElse(i18nIdentifier.getMessageKey());
    }
}
