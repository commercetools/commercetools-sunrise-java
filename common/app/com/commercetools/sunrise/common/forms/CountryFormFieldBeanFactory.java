package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.FormFieldFactory;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class CountryFormFieldBeanFactory extends FormFieldFactory<CountryFormFieldBean, CountryFormFieldBeanFactory.Data> {

    private final List<CountryCode> defaultCountries;
    private final CountryFormSelectableOptionBeanFactory countryFormSelectableOptionBeanFactory;

    @Inject
    public CountryFormFieldBeanFactory(final List<CountryCode> defaultCountries, final CountryFormSelectableOptionBeanFactory countryFormSelectableOptionBeanFactory) {
        this.defaultCountries = defaultCountries;
        this.countryFormSelectableOptionBeanFactory = countryFormSelectableOptionBeanFactory;
    }

    public final CountryFormFieldBean create(final Form<?> form, final String formFieldName, final List<CountryCode> availableCountries) {
        final Data data = new Data(form, formFieldName, availableCountries);
        return initializedViewModel(data);
    }

    public final CountryFormFieldBean createWithDefaultCountries(final Form<?> form, final String formFieldName) {
        final Data data = new Data(form, formFieldName, defaultCountries);
        return initializedViewModel(data);
    }

    @Override
    protected CountryFormFieldBean getViewModelInstance() {
        return new CountryFormFieldBean();
    }

    @Override
    protected final void initialize(final CountryFormFieldBean bean, final Data data) {
        fillList(bean, data);
    }

    protected void fillList(final CountryFormFieldBean bean, final Data data) {
        final String selectedCountryCode = FormUtils.extractFormField(data.form, data.formFieldName);
        bean.setList(data.availableCountries.stream()
                .map(country -> countryFormSelectableOptionBeanFactory.create(country, selectedCountryCode))
                .collect(toList()));
    }

    protected final static class Data extends Base {

        public final Form<?> form;
        public final String formFieldName;
        public final List<CountryCode> availableCountries;

        public Data(final Form<?> form, final String formFieldName, final List<CountryCode> availableCountries) {
            this.form = form;
            this.formFieldName = formFieldName;
            this.availableCountries = availableCountries;
        }
    }
}
