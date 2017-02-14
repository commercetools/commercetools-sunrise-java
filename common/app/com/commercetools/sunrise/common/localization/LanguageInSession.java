package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.sessions.DataFromResourceStoringOperations;
import com.commercetools.sunrise.common.sessions.SessionStrategy;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

/**
 * Keeps the current language in session.
 */
@RequestScoped
public class LanguageInSession extends DataFromResourceStoringOperations<Locale> {

    private static final String DEFAULT_LANGUAGE_SESSION_KEY = "sunrise-language";
    private final String languageSessionKey;
    private final SessionStrategy session;

    @Inject
    public LanguageInSession(final SessionStrategy session, final Configuration configuration) {
        this.languageSessionKey = configuration.getString("session.language", DEFAULT_LANGUAGE_SESSION_KEY);
        this.session = session;
    }

    public Optional<Locale> findCountry() {
        return session.findValueByKey(languageSessionKey).map(Locale::forLanguageTag);
    }

    @Override
    public void store(@Nullable final Locale locale) {
        super.store(locale);
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    protected void storeAssociatedData(final Locale locale) {
        session.overwriteValueByKey(languageSessionKey, locale.toLanguageTag());
    }

    @Override
    protected void removeAssociatedData() {
        session.removeValueByKey(languageSessionKey);
    }
}
