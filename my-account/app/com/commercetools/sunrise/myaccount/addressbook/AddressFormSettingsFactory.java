package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.common.forms.CountryFormFieldBeanFactory;
import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.inject.Inject;

public class AddressFormSettingsFactory extends Base {

    @Inject
    private TitleFormFieldBeanFactory titleFormFieldBeanFactory;
    @Inject
    private CountryFormFieldBeanFactory countryFormFieldBeanFactory;

    public AddressFormSettings create(final Form<?> form) {
        final AddressFormSettings bean = new AddressFormSettings();
        initialize(bean, form);
        return bean;
    }

    protected final void initialize(final AddressFormSettings bean, final Form<?> form) {
        fillTitle(bean, form);
        fillCountries(bean, form);
    }

    protected void fillCountries(final AddressFormSettings bean, final Form<?> form) {
        bean.setCountries(countryFormFieldBeanFactory.createWithDefaultCountries(form, "country"));
    }

    protected void fillTitle(final AddressFormSettings bean, final Form<?> form) {
        bean.setTitle(titleFormFieldBeanFactory.createWithDefaultTitles(form, "title"));
    }
}
