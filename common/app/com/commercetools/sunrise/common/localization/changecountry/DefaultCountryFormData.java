package com.commercetools.sunrise.common.localization.changecountry;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultCountryFormData extends Base implements CountryFormData {

    @Constraints.Required
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    @Override
    public CountryCode toCountryCode() {
        return CountryCode.valueOf(country);
    }
}
