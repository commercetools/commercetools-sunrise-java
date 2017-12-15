package com.commercetools.sunrise.core.localization;

import com.commercetools.sunrise.core.sessions.CookieSessionStrategy;
import com.commercetools.sunrise.core.sessions.DataFromResourceStoringOperations;
import com.commercetools.sunrise.core.sessions.SessionStrategy;
import com.neovisionaries.i18n.CountryCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Keeps the current country in session.
 */
@Singleton
public final class DefaultCountryInSession extends DataFromResourceStoringOperations<CountryCode> implements CountryInSession {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryInSession.class);

    private final String countryCookieName;
    private final SessionStrategy session;

    @Inject
    DefaultCountryInSession(final CookieSessionStrategy session, final Configuration configuration) {
        this.countryCookieName = Optional.ofNullable(configuration.getString("session.country"))
                .map(cookieName -> {
                    LOGGER.warn("session.country is deprecated, use sunrise.localization.countryCookieName instead");
                    return cookieName;
                }).orElseGet(() -> configuration.getString("sunrise.localization.countryCookieName"));
        this.session = session;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    public Optional<CountryCode> findCountry() {
        return session.findValueByKey(countryCookieName).map(CountryCode::getByCode);
    }

    @Override
    public void store(@Nullable final CountryCode countryCode) {
        super.store(countryCode);
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    protected void storeAssociatedData(final CountryCode countryCode) {
        session.overwriteValueByKey(countryCookieName, countryCode.getAlpha2());
    }

    @Override
    protected void removeAssociatedData() {
        session.removeValueByKey(countryCookieName);
    }
}
