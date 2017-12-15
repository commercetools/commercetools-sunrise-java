package com.commercetools.sunrise.core.i18n;

import io.sphere.sdk.models.LocalizedString;
import play.i18n.Lang;
import play.i18n.Langs;
import play.i18n.MessagesApi;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Singleton
final class I18nResolverImpl implements I18nResolver {

    private final MessagesApi messagesApi;
    private final Provider<Locale> localeProvider;
    private final Langs langs;

    @Inject
    I18nResolverImpl(final MessagesApi messagesApi, final Provider<Locale> localeProvider, final Langs langs) {
        this.messagesApi = messagesApi;
        this.localeProvider = localeProvider;
        this.langs = langs;
    }

    @Override
    public Locale currentLanguage() {
        return localeProvider.get();
    }

    @Override
    public Optional<String> get(final Locale locale, final LocalizedString localizedString) {
        return localizedString.find(locale)
                .map(Optional::of)
                .orElseGet(() -> localizedString.find(availableLocales()));
    }

    @Override
    public Optional<String> get(final Locale locale, final String messageKey, final Map<String, Object> args) {
        final Lang lang = new Lang(locale);
        final String key = transformDeprecatedKey(messageKey);
        return translate(lang, key, args);
    }

    private Optional<String> translate(final Lang lang, final String messageKey, final Map<String, Object> args) {
        if (messagesApi.isDefinedAt(lang, messageKey)) {
            return Optional.of(messagesApi.get(lang, messageKey, args));
        }
        return Optional.empty();
    }

    private String transformDeprecatedKey(final String messageKey) {
        return messageKey.replaceFirst(":", ".");
    }

    private List<Locale> availableLocales() {
        return langs.availables().stream()
                .map(Lang::toLocale)
                .collect(toList());
    }
}
