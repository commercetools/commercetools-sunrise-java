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
        final AddressFormSettings settings = new AddressFormSettings();
        fillTitle(settings, form);
        fillCountries(settings, form);
        return settings;
    }

    public void fillCountries(final AddressFormSettings settings, final Form<?> form) {
        settings.setCountries(countryFormFieldBeanFactory.createWithDefaultCountries(form, "country"));
    }

    public void fillTitle(final AddressFormSettings settings, final Form<?> form) {
        settings.setTitle(titleFormFieldBeanFactory.createWithDefaultTitles(form, "title"));
    }
}
