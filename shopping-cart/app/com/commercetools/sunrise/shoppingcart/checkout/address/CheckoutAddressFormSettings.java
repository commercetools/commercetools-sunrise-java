package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.models.CountryFormFieldBean;
import com.commercetools.sunrise.common.models.ModelBean;
import com.commercetools.sunrise.common.models.TitleFormFieldBean;

public class CheckoutAddressFormSettings extends ModelBean {

    private TitleFormFieldBean titleShipping;
    private TitleFormFieldBean titleBilling;
    private CountryFormFieldBean countriesShipping;
    private CountryFormFieldBean countriesBilling;

    public CheckoutAddressFormSettings() {
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
