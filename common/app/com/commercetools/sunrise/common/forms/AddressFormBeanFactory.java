package com.commercetools.sunrise.common.forms;

import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;

public abstract class AddressFormBeanFactory extends Base {

    protected AddressFormBean create(final Address address) {
        return fillBean(new AddressFormBean(), address);
    }

    protected <T extends AddressFormBean> T fillBean(final T bean, @Nullable final Address address) {
        if (address != null) {
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
        return bean;
    }

}
