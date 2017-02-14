package com.commercetools.sunrise.common.contexts;

import com.commercetools.sunrise.common.localization.CountryInSession;
import com.neovisionaries.i18n.CountryCode;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

public final class CountryFromSessionProvider implements Provider<CountryCode> {

    private final ProjectContext projectContext;
    private final CountryInSession countryInSession;

    @Inject
    public CountryFromSessionProvider(final ProjectContext projectContext, final CountryInSession countryInSession) {
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
