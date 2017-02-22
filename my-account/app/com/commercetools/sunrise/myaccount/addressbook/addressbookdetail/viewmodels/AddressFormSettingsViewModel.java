package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.common.models.ViewModel;
import com.commercetools.sunrise.common.models.addresses.CountryFormFieldViewModel;
import com.commercetools.sunrise.common.models.addresses.TitleFormFieldViewModel;

public class AddressFormSettingsViewModel extends ViewModel {

    private TitleFormFieldViewModel title;
    private CountryFormFieldViewModel countries;

    public AddressFormSettingsViewModel() {
    }

    public TitleFormFieldViewModel getTitle() {
        return title;
    }

    public void setTitle(final TitleFormFieldViewModel title) {
        this.title = title;
    }

    public CountryFormFieldViewModel getCountries() {
        return countries;
    }

    public void setCountries(final CountryFormFieldViewModel countries) {
        this.countries = countries;
    }
}
