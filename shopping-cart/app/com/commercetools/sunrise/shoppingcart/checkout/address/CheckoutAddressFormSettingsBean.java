package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.forms.CountryFormFieldBean;
import com.commercetools.sunrise.common.models.ViewModel;
import com.commercetools.sunrise.common.forms.TitleFormFieldBean;

public class CheckoutAddressFormSettingsBean extends ViewModel {

    private TitleFormFieldBean titleShipping;
    private TitleFormFieldBean titleBilling;
    private CountryFormFieldBean countriesShipping;
    private CountryFormFieldBean countriesBilling;

    public CheckoutAddressFormSettingsBean() {
    }

    public CountryFormFieldBean getCountriesBilling() {
        return countriesBilling;
    }

    public void setCountriesBilling(final CountryFormFieldBean countriesBilling) {
        this.countriesBilling = countriesBilling;
    }

    public CountryFormFieldBean getCountriesShipping() {
        return countriesShipping;
    }

    public void setCountriesShipping(final CountryFormFieldBean countriesShipping) {
        this.countriesShipping = countriesShipping;
    }

    public TitleFormFieldBean getTitleBilling() {
        return titleBilling;
    }

    public void setTitleBilling(final TitleFormFieldBean titleBilling) {
        this.titleBilling = titleBilling;
    }

    public TitleFormFieldBean getTitleShipping() {
        return titleShipping;
    }

    public void setTitleShipping(final TitleFormFieldBean titleShipping) {
        this.titleShipping = titleShipping;
    }
}
