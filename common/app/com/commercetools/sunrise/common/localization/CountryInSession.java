package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.sessions.DataFromResourceStoringOperations;
import com.commercetools.sunrise.common.sessions.SessionStrategy;
import com.neovisionaries.i18n.CountryCode;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Keeps the current country in session.
 */
@RequestScoped
public class CountryInSession extends DataFromResourceStoringOperations<CountryCode> {

    private static final String DEFAULT_COUNTRY_SESSION_KEY = "sunrise-country";
    private final String countrySessionKey;
    private final SessionStrategy session;

    @Inject
    public CountryInSession(final SessionStrategy session, final Configuration configuration) {
        this.countrySessionKey = configuration.getString("session.country", DEFAULT_COUNTRY_SESSION_KEY);
        this.session = session;
    }

    public Optional<CountryCode> findCountry() {
        return session.findValueByKey(countrySessionKey).map(CountryCode::getByCode);
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
        session.overwriteValueByKey(countrySessionKey, countryCode.getAlpha2());
    }

    @Override
    protected void removeAssociatedData() {
        session.removeValueByKey(countrySessionKey);
    }
}
