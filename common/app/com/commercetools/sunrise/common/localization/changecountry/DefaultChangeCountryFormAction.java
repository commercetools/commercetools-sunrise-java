package com.commercetools.sunrise.common.localization.changecountry;

import com.commercetools.sunrise.core.localization.CountryInSession;

import javax.inject.Inject;

public final class DefaultChangeCountryFormAction implements ChangeCountryFormAction {

    private final CountryInSession countryInSession;

    @Inject
    DefaultChangeCountryFormAction(final CountryInSession countryInSession) {
        this.countryInSession = countryInSession;
    }

    @Override
    public void accept(final ChangeCountryFormData formData) {
        countryInSession.store(formData.countryCode());
    }
}
