package com.commercetools.sunrise.myaccount.addressbook.changeaddress.view;

import com.commercetools.sunrise.common.models.AddressFormSettingsBeanFactory;
import com.commercetools.sunrise.common.models.FormPageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.inject.Inject;

public class ChangeAddressPageContentFactory extends FormPageContentFactory<ChangeAddressPageContent, Customer, AddressBookAddressFormData> {

    private final PageTitleResolver pageTitleResolver;
    private final AddressFormSettingsBeanFactory addressFormSettingsFactory;

    @Inject
    public ChangeAddressPageContentFactory(final PageTitleResolver pageTitleResolver, final AddressFormSettingsBeanFactory addressFormSettingsFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.addressFormSettingsFactory = addressFormSettingsFactory;
    }

    @Override
    protected ChangeAddressPageContent getViewModelInstance() {
        return new ChangeAddressPageContent();
    }

    @Override
    public final ChangeAddressPageContent create(final Customer customer, final Form<? extends AddressBookAddressFormData> form) {
        return super.create(customer, form);
    }

    @Override
    protected final void initialize(final ChangeAddressPageContent model, final Customer customer, final Form<? extends AddressBookAddressFormData> form) {
        super.initialize(model, customer, form);
        fillEditAddressForm(model, customer, form);
        fillEditAddressFormSettings(model, customer, form);
    }

    @Override
    protected void fillTitle(final ChangeAddressPageContent model, final Customer customer, final Form<? extends AddressBookAddressFormData> form) {
        model.setTitle(pageTitleResolver.getOrEmpty("myAccount:changeAddressPage.title"));
    }

    protected void fillEditAddressForm(final ChangeAddressPageContent model, final Customer customer, final Form<? extends AddressBookAddressFormData> form) {
        model.setEditAddressForm(form);
    }

    protected void fillEditAddressFormSettings(final ChangeAddressPageContent model, final Customer customer, final Form<? extends AddressBookAddressFormData> form) {
        model.setEditAddressFormSettings(addressFormSettingsFactory.create(form));
    }
}