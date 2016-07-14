package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.myaccount.addressbook.AddressFormSettingsFactory;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.inject.Inject;

public class ChangeAddressPageContentFactory extends Base {

    @Inject
    private AddressFormSettingsFactory addressFormSettingsFactory;

    public ChangeAddressPageContent create(final Form<?> form, final Customer customer) {
        return fillBean(new ChangeAddressPageContent(), form, customer);
    }

    protected <T extends ChangeAddressPageContent> T fillBean(final T content, final Form<?> form, final Customer customer) {
        fillEditAddressForm(content, form);
        return content;
    }

    protected void fillEditAddressForm(final ChangeAddressPageContent content, final Form<?> form) {
        content.setEditAddressForm(form);
        content.setEditAddressFormSettings(addressFormSettingsFactory.create(form));
    }
}