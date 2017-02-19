package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.common.models.ViewModel;
import com.commercetools.sunrise.common.models.addresses.CountryFormFieldBean;
import com.commercetools.sunrise.common.models.addresses.TitleFormFieldBean;

public class AddressFormSettingsBean extends ViewModel {

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
