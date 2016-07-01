package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.common.models.CountryFormFieldBean;
import com.commercetools.sunrise.common.models.ModelBean;
import com.commercetools.sunrise.common.models.TitleFormFieldBean;

public class AddressFormSettings extends ModelBean {

    private TitleFormFieldBean title;
    private CountryFormFieldBean countries;

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
