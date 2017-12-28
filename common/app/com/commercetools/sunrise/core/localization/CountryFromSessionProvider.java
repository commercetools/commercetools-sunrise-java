package com.commercetools.sunrise.core.localization;

import com.neovisionaries.i18n.CountryCode;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import static java.util.Collections.emptyList;

/**
 * Provides the {@link CountryCode} instance of the country saved in the user's session.
 * If not set, it provides the preferred country of the project.
 */
@Singleton
public final class CountryFromSessionProvider implements Provider<CountryCode> {

    private final CountryInSession countryInSession;
    private final CountryCode defaultCountry;

    @Inject
    CountryFromSessionProvider(final Countries countries, final CountryInSession countryInSession) {
        this.countryInSession = countryInSession;
        this.defaultCountry = countries.preferred(emptyList());
    }

    @Override
    public CountryCode get() {
        return countryInSession.findCountry().orElse(defaultCountry);
    }
}
