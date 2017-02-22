package com.commercetools.sunrise.common.models.addresses;

import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.contexts.ProjectContext;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.FormFieldViewModelFactory;
import com.neovisionaries.i18n.CountryCode;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class CountryFormFieldViewModelFactory extends FormFieldViewModelFactory<CountryFormFieldViewModel, CountryCode> {

    private final List<CountryCode> defaultCountries;
    private final CountryFormSelectableOptionViewModelFactory countryFormSelectableOptionViewModelFactory;

    @Inject
    public CountryFormFieldViewModelFactory(final ProjectContext projectContext, final CountryFormSelectableOptionViewModelFactory countryFormSelectableOptionViewModelFactory) {
        this.defaultCountries = projectContext.countries();
        this.countryFormSelectableOptionViewModelFactory = countryFormSelectableOptionViewModelFactory;
    }

    @Override
    protected CountryFormFieldViewModel getViewModelInstance() {
        return new CountryFormFieldViewModel();
    }

    @Override
    protected List<CountryCode> defaultOptions() {
        return defaultCountries;
    }

    @Override
    public final CountryFormFieldViewModel create(final FormFieldWithOptions<CountryCode> data) {
        return super.create(data);
    }

    @Override
    public final CountryFormFieldViewModel createWithDefaultOptions(final Form.Field data) {
        return super.createWithDefaultOptions(data);
    }

    @Override
    protected final void initialize(final CountryFormFieldViewModel model, final FormFieldWithOptions<CountryCode> data) {
        fillList(model, data);
    }

    protected void fillList(final CountryFormFieldViewModel model, final FormFieldWithOptions<CountryCode> formFieldWithOptions) {
        final CountryCode selectedCountry = CountryCode.getByCode(formFieldWithOptions.getFormField().value());
        model.setList(formFieldWithOptions.getFormOptions().stream()
                .map(country -> countryFormSelectableOptionViewModelFactory.create(country, selectedCountry))
                .collect(toList()));
    }
}
