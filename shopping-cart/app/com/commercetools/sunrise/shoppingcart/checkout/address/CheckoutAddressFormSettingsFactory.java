package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.forms.CountryFormFieldBeanFactory;
import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.inject.Inject;

import static java.util.Collections.singletonList;

public class CheckoutAddressFormSettingsFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private CountryFormFieldBeanFactory countryFormFieldBeanFactory;
    @Inject
    private TitleFormFieldBeanFactory titleFormFieldBeanFactory;

    public CheckoutAddressFormSettings create(final Form<?> form) {
        return fillBean(new CheckoutAddressFormSettings(), form);
    }

    protected <T extends CheckoutAddressFormSettings> T fillBean(final T bean, final Form<?> form) {
        fillCountriesShipping(bean, form);
        fillCountriesBilling(bean, form);
        fillTitleShipping(bean, form);
        fillTitleBilling(bean, form);
        return bean;
    }

    protected void fillTitleBilling(final CheckoutAddressFormSettings bean, final Form<?> form) {
        bean.setTitleBilling(titleFormFieldBeanFactory.createWithDefaultTitles(form, "titleBilling"));
    }

    protected void fillTitleShipping(final CheckoutAddressFormSettings bean, final Form<?> form) {
        bean.setTitleShipping(titleFormFieldBeanFactory.createWithDefaultTitles(form, "titleShipping"));
    }

    protected void fillCountriesBilling(final CheckoutAddressFormSettings bean, final Form<?> form) {
        bean.setCountriesBilling(countryFormFieldBeanFactory.createWithDefaultCountries(form, "countryBilling"));
    }

    protected void fillCountriesShipping(final CheckoutAddressFormSettings bean, final Form<?> form) {
        bean.setCountriesShipping(countryFormFieldBeanFactory.create(form, "countryShipping", singletonList(userContext.country())));
    }

}
