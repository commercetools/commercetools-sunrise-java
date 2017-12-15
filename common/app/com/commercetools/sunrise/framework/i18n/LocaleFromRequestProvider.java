package com.commercetools.sunrise.framework.i18n;

import play.i18n.Lang;
import play.i18n.Langs;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Locale;
import java.util.Optional;

import static java.util.Collections.emptyList;

/**
 * Provides the {@link Locale} instance from the language saved in context and the accepted languages from the user.
 */
@Singleton
public final class LocaleFromRequestProvider implements Provider<Locale> {

    private final Lang defaultLang;

    @Inject
    LocaleFromRequestProvider(final Langs langs) {
        this.defaultLang = langs.preferred(emptyList());
    }

    @Override
    public Locale get() {
        return Optional.ofNullable(Http.Context.current.get())
                .map(Http.Context::lang)
                .orElse(defaultLang)
                .toLocale();
    }
}