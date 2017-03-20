package com.commercetools.sunrise.framework.viewmodels.forms.countries;

import com.commercetools.sunrise.framework.localization.ProjectContext;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.forms.FormFieldViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.forms.FormFieldWithOptions;
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

    protected final List<CountryCode> getDefaultCountries() {
        return defaultCountries;
    }

    protected final CountryFormSelectableOptionViewModelFactory getCountryFormSelectableOptionViewModelFactory() {
        return countryFormSelectableOptionViewModelFactory;
    }

    @Override
    protected CountryFormFieldViewModel newViewModelInstance(final FormFieldWithOptions<CountryCode> formFieldWithOptions) {
        return new CountryFormFieldViewModel();
    }

    @Override
    protected List<CountryCode> defaultOptions() {
        return defaultCountries;
    }

    @Override
    public final CountryFormFieldViewModel create(final FormFieldWithOptions<CountryCode> formFieldWithOptions) {
        return super.create(formFieldWithOptions);
    }

    @Override
    public final CountryFormFieldViewModel createWithDefaultOptions(final Form.Field data) {
        return super.createWithDefaultOptions(data);
    }

    @Override
    protected final void initialize(final CountryFormFieldViewModel viewModel, final FormFieldWithOptions<CountryCode> formFieldWithOptions) {
        fillList(viewModel, formFieldWithOptions);
    }

    protected void fillList(final CountryFormFieldViewModel viewModel, final FormFieldWithOptions<CountryCode> formFieldWithOptions) {
        final CountryCode selectedCountry = CountryCode.getByCode(formFieldWithOptions.getFormField().value());
        viewModel.setList(formFieldWithOptions.getFormOptions().stream()
                .map(country -> countryFormSelectableOptionViewModelFactory.create(country, selectedCountry))
                .collect(toList()));
    }
}
