package com.commercetools.sunrise.common.contexts;

import com.neovisionaries.i18n.CountryCode;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static com.commercetools.sunrise.common.localization.SunriseLocalizationController.SESSION_COUNTRY;

public final class CountryFromSessionProvider implements Provider<CountryCode> {

    private final Http.Context httpContext;
    private final ProjectContext projectContext;

    @Inject
    public CountryFromSessionProvider(final Http.Context httpContext, final ProjectContext projectContext) {
        this.httpContext = httpContext;
        this.projectContext = projectContext;
    }

    @Override
    public CountryCode get() {
        return findCurrentCountry()
                .filter(projectContext::isCountrySupported)
                .orElseGet(projectContext::defaultCountry);
    }


    private Optional<CountryCode> findCurrentCountry() {
        return Optional.ofNullable(httpContext.session().get(SESSION_COUNTRY))
                .map(countryInSession -> CountryCode.getByCode(countryInSession, false));
    }
}
