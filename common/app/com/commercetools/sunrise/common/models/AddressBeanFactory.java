package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.UserContext;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class AddressBeanFactory extends Base {

    @Inject
    private UserContext userContext;

    public AddressBean create(@Nullable final Address address) {
        return fillBean(new AddressBean(), address);
    }

    protected <T extends AddressBean> T fillBean(final T bean, @Nullable final Address address) {
        if (address != null) {
            bean.setTitle(address.getTitle());
            bean.setFirstName(address.getFirstName());
            bean.setLastName(address.getLastName());
            bean.setStreetName(address.getStreetName());
            bean.setAdditionalStreetInfo(address.getAdditionalStreetInfo());
            bean.setCity(address.getCity());
            bean.setRegion(address.getRegion());
            bean.setPostalCode(address.getPostalCode());
            bean.setCountry(getDisplayCountry(address));
            bean.setPhone(address.getPhone());
            bean.setEmail(address.getEmail());
        }
        return bean;
    }

    private String getDisplayCountry(final Address address) {
        return address.getCountry().toLocale().getDisplayCountry(userContext.locale());
    }
}
