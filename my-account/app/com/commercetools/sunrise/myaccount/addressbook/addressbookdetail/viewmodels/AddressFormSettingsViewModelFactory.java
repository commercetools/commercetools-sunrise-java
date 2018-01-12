package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.core.viewmodels.FormViewModelFactory;
import com.commercetools.sunrise.core.viewmodels.forms.countries.CountryFormFieldViewModelFactory;
import com.commercetools.sunrise.core.viewmodels.forms.titles.TitleFormFieldViewModelFactory;
import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.myaccount.addressbook.AddAddressFormData;
import io.sphere.sdk.customers.Customer;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;

@RequestScoped
public class AddressFormSettingsViewModelFactory extends FormViewModelFactory<AddressFormSettingsViewModel, Customer, AddAddressFormData> {

    private final String titleFormFieldName;
    private final String countryFormFieldName;
    private final TitleFormFieldViewModelFactory titleFormFieldViewModelFactory;
    private final CountryFormFieldViewModelFactory countryFormFieldViewModelFactory;

    @Inject
    public AddressFormSettingsViewModelFactory(final Configuration configuration, final TitleFormFieldViewModelFactory titleFormFieldViewModelFactory,
                                               final CountryFormFieldViewModelFactory countryFormFieldViewModelFactory) {
        this.titleFormFieldName = configuration.getString("form.address.titleFormFieldName", "title");
        this.countryFormFieldName = configuration.getString("form.address.countryFormFieldName", "country");
        this.titleFormFieldViewModelFactory = titleFormFieldViewModelFactory;
        this.countryFormFieldViewModelFactory = countryFormFieldViewModelFactory;
    }

    protected final String getTitleFormFieldName() {
        return titleFormFieldName;
    }

    protected final String getCountryFormFieldName() {
        return countryFormFieldName;
    }

    protected final TitleFormFieldViewModelFactory getTitleFormFieldViewModelFactory() {
        return titleFormFieldViewModelFactory;
    }

    protected final CountryFormFieldViewModelFactory getCountryFormFieldViewModelFactory() {
        return countryFormFieldViewModelFactory;
    }

    @Override
    protected AddressFormSettingsViewModel newViewModelInstance(final Customer customer, final Form<? extends AddAddressFormData> form) {
        return new AddressFormSettingsViewModel();
    }

    @Override
    public final AddressFormSettingsViewModel create(final Customer customer, final Form<? extends AddAddressFormData> form) {
        return super.create(customer, form);
    }

    @Override
    protected final void initialize(final AddressFormSettingsViewModel viewModel, final Customer customer, final Form<? extends AddAddressFormData> form) {
        fillTitle(viewModel, customer, form);
        fillCountries(viewModel, customer, form);
    }

    protected void fillTitle(final AddressFormSettingsViewModel viewModel, final Customer customer, final Form<? extends AddAddressFormData> form) {
        viewModel.setTitle(titleFormFieldViewModelFactory.createWithDefaultOptions(form.field(titleFormFieldName)));
    }

    protected void fillCountries(final AddressFormSettingsViewModel viewModel, final Customer customer, final Form<? extends AddAddressFormData> form) {
        viewModel.setCountries(countryFormFieldViewModelFactory.createWithDefaultOptions(form.field(countryFormFieldName)));
    }
}
