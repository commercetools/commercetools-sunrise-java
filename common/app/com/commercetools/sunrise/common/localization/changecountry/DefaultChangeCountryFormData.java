package com.commercetools.sunrise.common.localization.changecountry;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.Required;

public class DefaultChangeCountryFormData extends Base implements ChangeCountryFormData {

    @Required
    private String country;

    @Override
    public CountryCode obtainCountryCode() {
        return CountryCode.valueOf(country);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }
}
