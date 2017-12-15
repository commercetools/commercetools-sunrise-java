package com.commercetools.sunrise.framework.localization;

import com.neovisionaries.i18n.CountryCode;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.List;

/**
 * Provides the {@link CountryCode} instance of the country saved in the user's session.
 * If not set, it provides the preferred country of the project.
 */
public final class CountryFromSessionProvider implements Provider<CountryCode> {

    private final Countries countries;
    private final CountryInSession countryInSession;

    @Inject
    CountryFromSessionProvider(final Countries countries, final CountryInSession countryInSession) {
        this.countries = countries;
        this.countryInSession = countryInSession;
    }

    @Override
    public CountryCode get() {
        return countries.preferred(candidates());
    }

    private List<CountryCode> candidates() {
        return countryInSession.findCountry()
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
    }
}
