package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.UserContext;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class AddressBeanFactory {

    @Inject
    private UserContext userContext;

    public AddressBean create(@Nullable final Address address) {
        final AddressBean bean = createAddressBean();
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

    protected AddressBean createAddressBean() {
        return new AddressBean();
    }

    private String getDisplayCountry(final Address address) {
        return address.getCountry().toLocale().getDisplayCountry(userContext.locale());
    }


}
