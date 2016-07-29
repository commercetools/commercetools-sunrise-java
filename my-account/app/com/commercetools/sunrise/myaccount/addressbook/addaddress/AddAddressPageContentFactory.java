package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.common.models.AddressFormSettingsBeanFactory;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.inject.Inject;

public class AddAddressPageContentFactory extends Base {

    @Inject
    private AddressFormSettingsBeanFactory addressFormSettingsBeanFactory;

    public AddAddressPageContent create(final Form<?> form, final Customer customer) {
        final AddAddressPageContent bean = new AddAddressPageContent();
        initialize(bean, form);
        return bean;
    }

    protected final void initialize(final AddAddressPageContent bean, final Form<?> form) {
        fillNewAddressForm(bean, form);
    }

    protected void fillNewAddressForm(final AddAddressPageContent bean, final Form<?> form) {
        bean.setNewAddressForm(form);
        bean.setNewAddressFormSettings(addressFormSettingsBeanFactory.create(form));
    }
}