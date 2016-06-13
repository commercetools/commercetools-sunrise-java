package com.commercetools.sunrise.common.localization;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class CountryFormData extends Base {

    @Constraints.Required
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }
}
