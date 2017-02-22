package com.commercetools.sunrise.myaccount.addressbook.changeaddress.viewmodels;

import com.commercetools.sunrise.common.models.AddressWithCustomer;
import com.commercetools.sunrise.common.models.FormPageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels.AddressFormSettingsViewModelFactory;
import play.data.Form;

import javax.inject.Inject;

public class ChangeAddressPageContentFactory extends FormPageContentFactory<ChangeAddressPageContent, AddressWithCustomer, AddressFormData> {

    private final PageTitleResolver pageTitleResolver;
    private final AddressFormSettingsViewModelFactory addressFormSettingsFactory;

    @Inject
    public ChangeAddressPageContentFactory(final PageTitleResolver pageTitleResolver, final AddressFormSettingsViewModelFactory addressFormSettingsFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.addressFormSettingsFactory = addressFormSettingsFactory;
    }

    @Override
    protected ChangeAddressPageContent getViewModelInstance() {
        return new ChangeAddressPageContent();
    }

    @Override
    public final ChangeAddressPageContent create(final AddressWithCustomer addressWithCustomer, final Form<? extends AddressFormData> form) {
        return super.create(addressWithCustomer, form);
    }

    @Override
    protected final void initialize(final ChangeAddressPageContent viewModel, final AddressWithCustomer addressWithCustomer, final Form<? extends AddressFormData> form) {
        super.initialize(viewModel, addressWithCustomer, form);
        fillEditAddressForm(viewModel, addressWithCustomer, form);
        fillEditAddressFormSettings(viewModel, addressWithCustomer, form);
    }

    @Override
    protected void fillTitle(final ChangeAddressPageContent viewModel, final AddressWithCustomer addressWithCustomer, final Form<? extends AddressFormData> form) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("myAccount:changeAddressPage.title"));
    }

    protected void fillEditAddressForm(final ChangeAddressPageContent viewModel, final AddressWithCustomer addressWithCustomer, final Form<? extends AddressFormData> form) {
        viewModel.setEditAddressForm(form);
    }

    protected void fillEditAddressFormSettings(final ChangeAddressPageContent viewModel, final AddressWithCustomer addressWithCustomer, final Form<? extends AddressFormData> form) {
        viewModel.setEditAddressFormSettings(addressFormSettingsFactory.create(addressWithCustomer.getCustomer(), form));
    }
}