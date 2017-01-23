package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.forms.CountryFormFieldBeanFactory;
import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import io.sphere.sdk.models.Base;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;

@RequestScoped
public class AddressFormSettingsBeanFactory extends ViewModelFactory<AddressFormSettingsBean, AddressFormSettingsBeanFactory.Data> {

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

    public final AddressFormSettingsBean create(final Form<?> form) {
        final Data data = new Data(form);
        return initializedViewModel(data);
    }

    @Override
    protected AddressFormSettingsBean getViewModelInstance() {
        return new AddressFormSettingsBean();
    }

    @Override
    protected final void initialize(final AddressFormSettingsBean bean, final Data data) {
        fillTitle(bean, data);
        fillCountries(bean, data);
    }

    protected void fillTitle(final AddressFormSettingsBean bean, final Data data) {
        bean.setTitle(titleFormFieldBeanFactory.createWithDefaultTitles(data.form, titleFormFieldName));
    }

    protected void fillCountries(final AddressFormSettingsBean bean, final Data data) {
        bean.setCountries(countryFormFieldBeanFactory.createWithDefaultCountries(data.form, countryFormFieldName));
    }

    protected final static class Data extends Base {

        public final Form<?> form;

        public Data(final Form<?> form) {
            this.form = form;
        }
    }
}
