package com.commercetools.sunrise.framework.localization;

import com.commercetools.sunrise.ctp.project.ProjectContext;
import com.commercetools.sunrise.sessions.country.CountryInSession;
import com.neovisionaries.i18n.CountryCode;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

/**
 * Provides the {@link CountryCode} instance of the country saved in the user's session.
 * If not set, it provides the default country from the project.
 */
public final class CountryFromSessionProvider implements Provider<CountryCode> {

    private final ProjectContext projectContext;
    private final CountryInSession countryInSession;

    @Inject
    CountryFromSessionProvider(final ProjectContext projectContext, final CountryInSession countryInSession) {
        this.projectContext = projectContext;
        this.countryInSession = countryInSession;
    }

    @Override
    public CountryCode get() {
        return findCurrentCountry()
                .filter(projectContext::isCountrySupported)
                .orElseGet(projectContext::defaultCountry);
    }


    private Optional<CountryCode> findCurrentCountry() {
        return countryInSession.findCountry();
    }
}
