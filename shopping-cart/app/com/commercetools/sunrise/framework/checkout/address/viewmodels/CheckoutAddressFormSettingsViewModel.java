package com.commercetools.sunrise.framework.checkout.address.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.forms.countries.CountryFormFieldViewModel;
import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import com.commercetools.sunrise.framework.viewmodels.forms.titles.TitleFormFieldViewModel;

public class CheckoutAddressFormSettingsViewModel extends ViewModel {

    private TitleFormFieldViewModel titleShipping;
    private TitleFormFieldViewModel titleBilling;
    private CountryFormFieldViewModel countriesShipping;
    private CountryFormFieldViewModel countriesBilling;

    public CheckoutAddressFormSettingsViewModel() {
    }

    public CountryFormFieldViewModel getCountriesBilling() {
        return countriesBilling;
    }

    public void setCountriesBilling(final CountryFormFieldViewModel countriesBilling) {
        this.countriesBilling = countriesBilling;
    }

    public CountryFormFieldViewModel getCountriesShipping() {
        return countriesShipping;
    }

    public void setCountriesShipping(final CountryFormFieldViewModel countriesShipping) {
        this.countriesShipping = countriesShipping;
    }

    public TitleFormFieldViewModel getTitleBilling() {
        return titleBilling;
    }

    public void setTitleBilling(final TitleFormFieldViewModel titleBilling) {
        this.titleBilling = titleBilling;
    }

    public TitleFormFieldViewModel getTitleShipping() {
        return titleShipping;
    }

    public void setTitleShipping(final TitleFormFieldViewModel titleShipping) {
        this.titleShipping = titleShipping;
    }
}
