package com.commercetools.sunrise.common.localization;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class CountryFormData extends Base {

    @Constraints.Required
    private String country;

    public String getCountry() {
        return country;
    }

    public CountryCode toCountryCode() {
        return CountryCode.valueOf(country);
    }

    public void setCountry(final String country) {
        this.country = country;
    }
}
