package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class AddressBeanFactory extends Base {

    private final Locale locale;

    @Inject
    public AddressBeanFactory(final Locale locale) {
        this.locale = locale;
    }

    public AddressBean create(@Nullable final Address address) {
        final AddressBean bean = new AddressBean();
        initialize(bean, address);
        return bean;
    }

    protected final void initialize(final AddressBean bean, @Nullable final Address address) {
        if (address != null) {
            fillTitle(bean, address);
            fillFirstName(bean, address);
            fillLastName(bean, address);
            fillStreetName(bean, address);
            fillAdditionalStreetInfo(bean, address);
            fillCity(bean, address);
            fillRegion(bean, address);
            fillPostalCode(bean, address);
            fillCountry(bean, address);
            fillPhone(bean, address);
            fillEmail(bean, address);
        }
    }

    protected void fillTitle(final AddressBean bean, final Address address) {
        bean.setTitle(address.getTitle());
    }

    protected void fillFirstName(final AddressBean bean, final Address address) {
        bean.setFirstName(address.getFirstName());
    }

    protected void fillLastName(final AddressBean bean, final Address address) {
        bean.setLastName(address.getLastName());
    }

    protected void fillStreetName(final AddressBean bean, final Address address) {
        bean.setStreetName(address.getStreetName());
    }

    protected void fillAdditionalStreetInfo(final AddressBean bean, final Address address) {
        bean.setAdditionalStreetInfo(address.getAdditionalStreetInfo());
    }

    protected void fillCity(final AddressBean bean, final Address address) {
        bean.setCity(address.getCity());
    }

    protected void fillRegion(final AddressBean bean, final Address address) {
        bean.setRegion(address.getRegion());
    }

    protected void fillPostalCode(final AddressBean bean, final Address address) {
        bean.setPostalCode(address.getPostalCode());
    }

    protected void fillEmail(final AddressBean bean, final Address address) {
        bean.setEmail(address.getEmail());
    }

    protected void fillPhone(final AddressBean bean, final Address address) {
        bean.setPhone(address.getPhone());
    }

    protected void fillCountry(final AddressBean bean, final Address address) {
        bean.setCountry(address.getCountry().toLocale().getDisplayCountry(locale));
    }
}
