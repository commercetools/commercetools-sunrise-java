package com.commercetools.sunrise.myaccount.addressbook.changeaddress.viewmodels;

import com.commercetools.sunrise.common.models.AddressWithCustomer;
import com.commercetools.sunrise.common.models.FormPageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels.AddressFormSettingsBeanFactory;
import play.data.Form;

import javax.inject.Inject;

public class ChangeAddressPageContentFactory extends FormPageContentFactory<ChangeAddressPageContent, AddressWithCustomer, AddressFormData> {

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
    public final ChangeAddressPageContent create(final AddressWithCustomer addressWithCustomer, final Form<? extends AddressFormData> form) {
        return super.create(addressWithCustomer, form);
    }

    @Override
    protected final void initialize(final ChangeAddressPageContent model, final AddressWithCustomer addressWithCustomer, final Form<? extends AddressFormData> form) {
        super.initialize(model, addressWithCustomer, form);
        fillEditAddressForm(model, addressWithCustomer, form);
        fillEditAddressFormSettings(model, addressWithCustomer, form);
    }

    @Override
    protected void fillTitle(final ChangeAddressPageContent model, final AddressWithCustomer addressWithCustomer, final Form<? extends AddressFormData> form) {
        model.setTitle(pageTitleResolver.getOrEmpty("myAccount:changeAddressPage.title"));
    }

    protected void fillEditAddressForm(final ChangeAddressPageContent model, final AddressWithCustomer addressWithCustomer, final Form<? extends AddressFormData> form) {
        model.setEditAddressForm(form);
    }

    protected void fillEditAddressFormSettings(final ChangeAddressPageContent model, final AddressWithCustomer addressWithCustomer, final Form<? extends AddressFormData> form) {
        model.setEditAddressFormSettings(addressFormSettingsFactory.create(addressWithCustomer.getCustomer(), form));
    }
}