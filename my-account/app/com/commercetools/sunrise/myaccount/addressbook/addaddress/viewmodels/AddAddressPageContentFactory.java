package com.commercetools.sunrise.myaccount.addressbook.addaddress.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.models.addresses.AddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels.AddressFormSettingsViewModelFactory;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.inject.Inject;

public class AddAddressPageContentFactory extends FormPageContentFactory<AddAddressPageContent, Customer, AddressFormData> {

    private final AddressFormSettingsViewModelFactory addressFormSettingsViewModelFactory;

    @Inject
    public AddAddressPageContentFactory(final AddressFormSettingsViewModelFactory addressFormSettingsViewModelFactory) {
        this.addressFormSettingsViewModelFactory = addressFormSettingsViewModelFactory;
    }

    protected final AddressFormSettingsViewModelFactory getAddressFormSettingsViewModelFactory() {
        return addressFormSettingsViewModelFactory;
    }

    @Override
    protected AddAddressPageContent newViewModelInstance(final Customer customer, final Form<? extends AddressFormData> form) {
        return new AddAddressPageContent();
    }

    @Override
    public final AddAddressPageContent create(final Customer customer, final Form<? extends AddressFormData> form) {
        return super.create(customer, form);
    }

    @Override
    protected final void initialize(final AddAddressPageContent viewModel, final Customer customer, final Form<? extends AddressFormData> form) {
        super.initialize(viewModel, customer, form);
        fillNewAddressForm(viewModel, customer, form);
        fillNewAddressFormSettings(viewModel, customer, form);
    }

    protected void fillNewAddressForm(final AddAddressPageContent viewModel, final Customer customer, final Form<? extends AddressFormData> form) {
        viewModel.setNewAddressForm(form);
    }

    protected void fillNewAddressFormSettings(final AddAddressPageContent viewModel, final Customer customer, final Form<? extends AddressFormData> form) {
        viewModel.setNewAddressFormSettings(addressFormSettingsViewModelFactory.create(customer, form));
    }
}