package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.forms.CountryFormFieldBeanFactory;
import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.inject.Inject;

import static java.util.Collections.singletonList;

public class CheckoutAddressFormSettingsBeanFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private CountryFormFieldBeanFactory countryFormFieldBeanFactory;
    @Inject
    private TitleFormFieldBeanFactory titleFormFieldBeanFactory;

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
        bean.setCountriesShipping(countryFormFieldBeanFactory.create(form, "countryShipping", singletonList(userContext.country())));
    }

}
