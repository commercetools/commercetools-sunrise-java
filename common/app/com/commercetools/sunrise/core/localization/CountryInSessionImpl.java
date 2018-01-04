package com.commercetools.sunrise.core.localization;

import com.commercetools.sunrise.core.sessions.StoringStrategy;
import com.neovisionaries.i18n.CountryCode;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Keeps the current country in session.
 */
@Singleton
final class CountryInSessionImpl implements CountryInSession {

    private final String cookieName;
    private final boolean cookieSecure;
    private final boolean cookieHttpOnly;
    private final StoringStrategy storingStrategy;

    @Inject
    CountryInSessionImpl(final Configuration globalConfig, final StoringStrategy storingStrategy) {
        final Configuration config = globalConfig.getConfig("sunrise.localization");
        this.cookieName = config.getString("countryCookieName");
        this.cookieSecure = config.getBoolean("countryCookieHttpOnly");
        this.cookieHttpOnly = config.getBoolean("countryCookieHttpOnly");
        this.storingStrategy = storingStrategy;
    }

    @Override
    public Optional<CountryCode> findCountry() {
        return storingStrategy.findInCookies(cookieName)
                .map(CountryCode::getByCode)
                .filter(country -> country != null && !country.equals(CountryCode.UNDEFINED));
    }

    @Override
    public void store(@Nullable final CountryCode countryCode) {
        final String country = countryCode != null ? countryCode.getAlpha2() : null;
        storingStrategy.overwriteInCookies(cookieName, country, cookieHttpOnly, cookieSecure);
    }

    @Override
    public void remove() {
        storingStrategy.removeFromCookies(cookieName);
    }
}
