package com.commercetools.sunrise.common.forms;

import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;

public abstract class AddressFormBeanFactory extends Base {

    protected AddressFormBean create(final Address address) {
        final AddressFormBean bean = new AddressFormBean();
        initialize(bean, address);
        return bean;
    }

    protected final void initialize(final AddressFormBean bean, @Nullable final Address address) {
        if (address != null) {
            fillAddressFields(bean, address);
        }
    }

    protected void fillAddressFields(final AddressFormBean bean, final Address address) {
        bean.setFirstName(address.getFirstName());
        bean.setLastName(address.getLastName());
        bean.setStreetName(address.getStreetName());
        bean.setAdditionalStreetInfo(address.getAdditionalStreetInfo());
        bean.setCity(address.getCity());
        bean.setPostalCode(address.getPostalCode());
        bean.setRegion(address.getRegion());
        bean.setPhone(address.getPhone());
        bean.setEmail(address.getEmail());
    }

}
