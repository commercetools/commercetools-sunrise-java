package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.common.controllers.WithOverridablePageContent;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormSettingsFactory;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.inject.Inject;

public class AddAddressPageContentFactory extends Base implements WithOverridablePageContent<AddAddressPageContent> {

    @Inject
    private AddressFormSettingsFactory addressFormSettingsFactory;

    public AddAddressPageContent create(final Form<?> form, final Customer customer) {
        final AddAddressPageContent content = createPageContent();
        fillNewAddressForm(content, form);
        return content;
    }

    protected void fillNewAddressForm(final AddAddressPageContent content, final Form<?> form) {
        content.setNewAddressForm(form);
        content.setNewAddressFormSettings(addressFormSettingsFactory.create(form));
    }

    @Override
    public AddAddressPageContent createPageContent() {
        return new AddAddressPageContent();
    }
}