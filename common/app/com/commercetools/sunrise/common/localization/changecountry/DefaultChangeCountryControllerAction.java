package com.commercetools.sunrise.common.localization.changecountry;

import com.commercetools.sunrise.sessions.country.CountryInSession;

import javax.inject.Inject;

public final class DefaultChangeCountryControllerAction implements ChangeCountryControllerAction {

    private final CountryInSession countryInSession;

    @Inject
    DefaultChangeCountryControllerAction(final CountryInSession countryInSession) {
        this.countryInSession = countryInSession;
    }

    @Override
    public void accept(final ChangeCountryFormData formData) {
        countryInSession.store(formData.countryCode());
    }
}
