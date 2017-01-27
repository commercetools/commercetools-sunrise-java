package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.AddressFormSettingsBeanFactory;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;

import javax.inject.Inject;

@RequestScoped
public class ChangeAddressPageContentFactory extends PageContentFactory<ChangeAddressPageContent, ChangeAddressControllerData> {

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
    public final ChangeAddressPageContent create(final ChangeAddressControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ChangeAddressPageContent model, final ChangeAddressControllerData data) {
        super.initialize(model, data);
        fillEditAddressForm(model, data);
        fillEditAddressFormSettings(model, data);
    }

    @Override
    protected void fillTitle(final ChangeAddressPageContent model, final ChangeAddressControllerData data) {
        model.setTitle(pageTitleResolver.getOrEmpty("myAccount:changeAddressPage.title"));
    }

    protected void fillEditAddressForm(final ChangeAddressPageContent model, final ChangeAddressControllerData data) {
        model.setEditAddressForm(data.getForm());
    }

    protected void fillEditAddressFormSettings(final ChangeAddressPageContent model, final ChangeAddressControllerData data) {
        model.setEditAddressFormSettings(addressFormSettingsFactory.create(data.getForm()));
    }
}