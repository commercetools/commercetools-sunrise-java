package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.forms.CountryFormFieldBeanFactory;
import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.neovisionaries.i18n.CountryCode;
import play.data.Form;

import javax.inject.Inject;

import static java.util.Collections.singletonList;

@RequestScoped
public class CheckoutAddressFormSettingsBeanFactory extends ViewModelFactory {

    private final CountryCode country;
    private final CountryFormFieldBeanFactory countryFormFieldBeanFactory;
    private final TitleFormFieldBeanFactory titleFormFieldBeanFactory;

    @Inject
    public CheckoutAddressFormSettingsBeanFactory(final CountryCode country, final CountryFormFieldBeanFactory countryFormFieldBeanFactory,
                                                  final TitleFormFieldBeanFactory titleFormFieldBeanFactory) {
        this.country = country;
        this.countryFormFieldBeanFactory = countryFormFieldBeanFactory;
        this.titleFormFieldBeanFactory = titleFormFieldBeanFactory;
    }

    public CheckoutAddressFormSettingsBean create(final Form<?> form) {
        final CheckoutAddressFormSettingsBean bean = new CheckoutAddressFormSettingsBean();
        initialize(bean, form);
        return bean;
    }

    protected final void initialize(final CheckoutAddressFormSettingsBean bean, final Form<?> form) {
        fillCountriesShipping(bean, form);
        fillCountriesBilling(bean, form);
        fillTitleShipping(bean, form);
        fillTitleBilling(bean, form);
    }

    protected void fillTitleBilling(final CheckoutAddressFormSettingsBean bean, final Form<?> form) {
        bean.setTitleBilling(titleFormFieldBeanFactory.createWithDefaultTitles(form, "titleBilling"));
    }

    protected void fillTitleShipping(final CheckoutAddressFormSettingsBean bean, final Form<?> form) {
        bean.setTitleShipping(titleFormFieldBeanFactory.createWithDefaultTitles(form, "titleShipping"));
    }

    protected void fillCountriesBilling(final CheckoutAddressFormSettingsBean bean, final Form<?> form) {
        bean.setCountriesBilling(countryFormFieldBeanFactory.createWithDefaultCountries(form, "countryBilling"));
    }

    protected void fillCountriesShipping(final CheckoutAddressFormSettingsBean bean, final Form<?> form) {
        bean.setCountriesShipping(countryFormFieldBeanFactory.create(form, "countryShipping", singletonList(country)));
    }

}
