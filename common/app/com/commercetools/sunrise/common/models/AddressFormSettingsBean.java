package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.forms.CountryFormFieldBean;
import com.commercetools.sunrise.common.forms.TitleFormFieldBean;

public class AddressFormSettingsBean extends ModelBean {

    private TitleFormFieldBean title;
    private CountryFormFieldBean countries;

    public AddressFormSettingsBean() {
    }

    public TitleFormFieldBean getTitle() {
        return title;
    }

    public void setTitle(final TitleFormFieldBean title) {
        this.title = title;
    }

    public CountryFormFieldBean getCountries() {
        return countries;
    }

    public void setCountries(final CountryFormFieldBean countries) {
        this.countries = countries;
    }
}
