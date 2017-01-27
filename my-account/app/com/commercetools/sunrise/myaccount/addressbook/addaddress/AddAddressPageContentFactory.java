package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.AddressFormSettingsBeanFactory;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;

import javax.inject.Inject;

@RequestScoped
public class AddAddressPageContentFactory extends PageContentFactory<AddAddressPageContent, AddAddressControllerData> {

    private final PageTitleResolver pageTitleResolver;
    private final AddressFormSettingsBeanFactory addressFormSettingsBeanFactory;

    @Inject
    public AddAddressPageContentFactory(final PageTitleResolver pageTitleResolver, final AddressFormSettingsBeanFactory addressFormSettingsBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.addressFormSettingsBeanFactory = addressFormSettingsBeanFactory;
    }

    @Override
    protected AddAddressPageContent getViewModelInstance() {
        return new AddAddressPageContent();
    }

    @Override
    public final AddAddressPageContent create(final AddAddressControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final AddAddressPageContent model, final AddAddressControllerData data) {
        super.initialize(model, data);
        fillNewAddressForm(model, data);
        fillNewAddressFormSettings(model, data);
    }

    @Override
    protected void fillTitle(final AddAddressPageContent model, final AddAddressControllerData data) {
        model.setTitle(pageTitleResolver.getOrEmpty("myAccount:addAddressPage.title"));
    }

    protected void fillNewAddressForm(final AddAddressPageContent model, final AddAddressControllerData data) {
        model.setNewAddressForm(data.getForm());
    }

    protected void fillNewAddressFormSettings(final AddAddressPageContent model, final AddAddressControllerData data) {
        model.setNewAddressFormSettings(addressFormSettingsBeanFactory.create(data.getForm()));
    }
}