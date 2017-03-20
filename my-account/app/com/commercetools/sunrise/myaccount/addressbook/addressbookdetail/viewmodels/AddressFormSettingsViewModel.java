package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import com.commercetools.sunrise.framework.viewmodels.forms.countries.CountryFormFieldViewModel;
import com.commercetools.sunrise.framework.viewmodels.forms.titles.TitleFormFieldViewModel;

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
