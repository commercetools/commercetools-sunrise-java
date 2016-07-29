package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.forms.CountryFormFieldBeanFactory;
import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.inject.Inject;

public class AddressFormSettingsBeanFactory extends Base {

    @Inject
    private TitleFormFieldBeanFactory titleFormFieldBeanFactory;
    @Inject
    private CountryFormFieldBeanFactory countryFormFieldBeanFactory;

    public AddressFormSettingsBean create(final Form<?> form) {
        final AddressFormSettingsBean bean = new AddressFormSettingsBean();
        initialize(bean, form);
        return bean;
    }

    protected final void initialize(final AddressFormSettingsBean bean, final Form<?> form) {
        fillTitle(bean, form);
        fillCountries(bean, form);
    }

    protected void fillCountries(final AddressFormSettingsBean bean, final Form<?> form) {
        bean.setCountries(countryFormFieldBeanFactory.createWithDefaultCountries(form, "country"));
    }

    protected void fillTitle(final AddressFormSettingsBean bean, final Form<?> form) {
        bean.setTitle(titleFormFieldBeanFactory.createWithDefaultTitles(form, "title"));
    }
}
