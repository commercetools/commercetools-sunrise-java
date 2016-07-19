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
        final ChangeAddressPageContent bean = new ChangeAddressPageContent();
        initialize(bean, form);
        return bean;
    }

    protected final void initialize(final ChangeAddressPageContent bean, final Form<?> form) {
        fillEditAddressForm(bean, form);
    }

    protected void fillEditAddressForm(final ChangeAddressPageContent bean, final Form<?> form) {
        bean.setEditAddressForm(form);
        bean.setEditAddressFormSettings(addressFormSettingsFactory.create(form));
    }
}