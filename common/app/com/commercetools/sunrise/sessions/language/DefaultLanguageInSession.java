package com.commercetools.sunrise.sessions.language;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.sessions.DataFromResourceStoringOperations;
import com.commercetools.sunrise.sessions.SessionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

/**
 * Keeps the current language in session.
 */
@RequestScoped
public class DefaultLanguageInSession extends DataFromResourceStoringOperations<Locale> implements LanguageInSession {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageInSession.class);
    private static final String DEFAULT_LANGUAGE_SESSION_KEY = "sunrise-language";

    private final String languageSessionKey;
    private final SessionStrategy session;

    @Inject
    public DefaultLanguageInSession(final SessionStrategy session, final Configuration configuration) {
        this.languageSessionKey = configuration.getString("session.language", DEFAULT_LANGUAGE_SESSION_KEY);
        this.session = session;
    }

    @Override
    protected final Logger getLogger() {
        return LOGGER;
    }

    protected final SessionStrategy getSession() {
        return session;
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
