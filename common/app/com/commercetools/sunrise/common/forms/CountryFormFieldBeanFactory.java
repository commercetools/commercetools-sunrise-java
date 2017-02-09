package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.FormFieldViewModelFactory;
import com.neovisionaries.i18n.CountryCode;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class CountryFormFieldBeanFactory extends FormFieldViewModelFactory<CountryFormFieldBean, CountryCode> {

    private final List<CountryCode> defaultCountries;
    private final CountryFormSelectableOptionBeanFactory countryFormSelectableOptionBeanFactory;

    @Inject
    public CountryFormFieldBeanFactory(final ProjectContext projectContext, final CountryFormSelectableOptionBeanFactory countryFormSelectableOptionBeanFactory) {
        this.defaultCountries = projectContext.countries();
        this.countryFormSelectableOptionBeanFactory = countryFormSelectableOptionBeanFactory;
    }

    @Override
    protected CountryFormFieldBean getViewModelInstance() {
        return new CountryFormFieldBean();
    }

    @Override
    protected List<CountryCode> defaultOptions() {
        return defaultCountries;
    }

    @Override
    public final CountryFormFieldBean create(final FormFieldWithOptions<CountryCode> data) {
        return super.create(data);
    }

    @Override
    public final CountryFormFieldBean createWithDefaultOptions(final Form.Field data) {
        return super.createWithDefaultOptions(data);
    }

    @Override
    protected final void initialize(final CountryFormFieldBean model, final FormFieldWithOptions<CountryCode> data) {
        fillList(model, data);
    }

    protected void fillList(final CountryFormFieldBean bean, final FormFieldWithOptions<CountryCode> formFieldWithOptions) {
        final CountryCode selectedCountry = CountryCode.getByCode(formFieldWithOptions.getFormField().value());
        bean.setList(formFieldWithOptions.getFormOptions().stream()
                .map(country -> countryFormSelectableOptionBeanFactory.create(country, selectedCountry))
                .collect(toList()));
    }
}
