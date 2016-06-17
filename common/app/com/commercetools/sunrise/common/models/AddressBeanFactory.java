package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.UserContext;
import io.sphere.sdk.models.Address;

import javax.inject.Inject;

public class AddressBeanFactory {

    @Inject
    private UserContext userContext;

    public AddressBean create(final Address address) {
        final AddressBean bean = new AddressBean();
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
        return bean;
    }

    private String getDisplayCountry(final Address address) {
        return address.getCountry().toLocale().getDisplayCountry(userContext.locale());
    }


}
