package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressFormSettingsFactory;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.inject.Inject;

public class ChangeAddressPageContentFactory {

    @Inject
    private AddressFormSettingsFactory addressFormSettingsFactory;

    public ChangeAddressPageContent create(final Form<?> form, final Customer customer) {
        final ChangeAddressPageContent content = new ChangeAddressPageContent();
        fillEditAddressForm(content, form);
        return content;
    }

    protected void fillEditAddressForm(final ChangeAddressPageContent content, final Form<?> form) {
        content.setEditAddressForm(form);
        content.setEditAddressFormSettings(addressFormSettingsFactory.create(form));
    }
}