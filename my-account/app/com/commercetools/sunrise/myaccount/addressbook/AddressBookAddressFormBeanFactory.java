package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.forms.AddressFormBeanFactory;
import com.commercetools.sunrise.common.forms.FormUtils;
import io.sphere.sdk.models.Address;
import play.data.Form;

import javax.inject.Inject;

public class AddressBookAddressFormBeanFactory extends AddressFormBeanFactory {

    @Inject
    private ProjectContext projectContext;

    public AddressBookAddressFormBean create(final Form<?> form) {
        final AddressBookAddressFormBean bean = new AddressBookAddressFormBean();
        final Address address = FormUtils.extractAddress(form);
        fillAddressForm(bean, address, projectContext.countries());
        fillDefaultAddresses(form, bean);
        return bean;
    }

    private void fillDefaultAddresses(final Form<?> form, final AddressBookAddressFormBean bean) {
        final boolean defaultBillingAddress = FormUtils.extractBooleanFormField(form, "defaultBillingAddress");
        bean.setDefaultBillingAddress(defaultBillingAddress);
        final boolean defaultShippingAddress = FormUtils.extractBooleanFormField(form, "defaultShippingAddress");
        bean.setDefaultShippingAddress(defaultShippingAddress);
    }
}
