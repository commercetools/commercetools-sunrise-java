package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.forms.CountryFormFieldBeanFactory;
import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;

@RequestScoped
public class AddressFormSettingsBeanFactory extends ViewModelFactory<AddressFormSettingsBean, Form<?>> {

    private final String titleFormFieldName;
    private final String countryFormFieldName;
    private final TitleFormFieldBeanFactory titleFormFieldBeanFactory;
    private final CountryFormFieldBeanFactory countryFormFieldBeanFactory;

    @Inject
    public AddressFormSettingsBeanFactory(final Configuration configuration, final TitleFormFieldBeanFactory titleFormFieldBeanFactory,
                                          final CountryFormFieldBeanFactory countryFormFieldBeanFactory) {
        this.titleFormFieldName = configuration.getString("form.address.titleFormFieldName", "title");
        this.countryFormFieldName = configuration.getString("form.address.countryFormFieldName", "country");
        this.titleFormFieldBeanFactory = titleFormFieldBeanFactory;
        this.countryFormFieldBeanFactory = countryFormFieldBeanFactory;
    }

    @Override
    protected AddressFormSettingsBean getViewModelInstance() {
        return new AddressFormSettingsBean();
    }

    @Override
    public final AddressFormSettingsBean create(final Form<?> data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final AddressFormSettingsBean model, final Form<?> data) {
        fillTitle(model, data);
        fillCountries(model, data);
    }

    protected void fillTitle(final AddressFormSettingsBean model, final Form<?> form) {
        model.setTitle(titleFormFieldBeanFactory.createWithDefaultTitles(form, titleFormFieldName));
    }

    protected void fillCountries(final AddressFormSettingsBean model, final Form<?> form) {
        model.setCountries(countryFormFieldBeanFactory.createWithDefaultCountries(form, countryFormFieldName));
    }
}
