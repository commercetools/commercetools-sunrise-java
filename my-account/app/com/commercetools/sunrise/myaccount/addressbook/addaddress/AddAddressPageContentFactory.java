package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressFormSettingsFactory;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.inject.Inject;

public class AddAddressPageContentFactory extends Base {

    @Inject
    private AddressFormSettingsFactory addressFormSettingsFactory;

    public AddAddressPageContent create(final Form<?> form, final Customer customer) {
        return fillBean(new AddAddressPageContent(), form, customer);
    }

    protected <T extends AddAddressPageContent> T fillBean(final T content, final Form<?> form, final Customer customer) {
        content.setNewAddressForm(form);
        content.setNewAddressFormSettings(addressFormSettingsFactory.create(form));
        return content;
    }
}