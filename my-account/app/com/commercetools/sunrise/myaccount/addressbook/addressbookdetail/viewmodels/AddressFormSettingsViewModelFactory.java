package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.common.models.FormViewModelFactory;
import com.commercetools.sunrise.common.models.addresses.CountryFormFieldViewModelFactory;
import com.commercetools.sunrise.common.models.addresses.TitleFormFieldViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormData;
import io.sphere.sdk.customers.Customer;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;

@RequestScoped
public class AddressFormSettingsViewModelFactory extends FormViewModelFactory<AddressFormSettingsViewModel, Customer, AddressFormData> {

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

    @Override
    protected AddressFormSettingsViewModel getViewModelInstance() {
        return new AddressFormSettingsViewModel();
    }

    @Override
    public final AddressFormSettingsViewModel create(final Customer customer, final Form<? extends AddressFormData> form) {
        return super.create(customer, form);
    }

    @Override
    protected final void initialize(final AddressFormSettingsViewModel viewModel, final Customer customer, final Form<? extends AddressFormData> form) {
        fillTitle(viewModel, customer, form);
        fillCountries(viewModel, customer, form);
    }

    protected void fillTitle(final AddressFormSettingsViewModel model, final Customer customer, final Form<? extends AddressFormData> form) {
        model.setTitle(titleFormFieldViewModelFactory.createWithDefaultOptions(form.field(titleFormFieldName)));
    }

    protected void fillCountries(final AddressFormSettingsViewModel model, final Customer customer, final Form<? extends AddressFormData> form) {
        model.setCountries(countryFormFieldViewModelFactory.createWithDefaultOptions(form.field(countryFormFieldName)));
    }
}
