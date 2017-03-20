package com.commercetools.sunrise.common.localization.changecountry;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.Required;

public class DefaultChangeCountryFormData extends Base implements ChangeCountryFormData {

    @Required
    private String country;

    @Override
    public CountryCode countryCode() {
        return CountryCode.valueOf(country);
    }


    // Getters & setters

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }
}
