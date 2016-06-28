package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

public abstract class AddressFormBeanFactory {

    @Inject
    private UserContext userContext;
    @Inject
    private ProjectContext projectContext;
    @Inject
    private TitleFormFieldBeanFactory titleFormFieldBeanFactory;
    @Inject
    private CountryFormFieldBeanFactory countryFormFieldBeanFactory;

    protected AddressFormBean create(final Address address) {
        final AddressFormBean bean = new AddressFormBean();
        fillAddressForm(bean, address, projectContext.countries());
        return bean;
    }

    protected void fillAddressForm(final AddressFormBean bean, @Nullable final Address address, final List<CountryCode> availableCountries) {
        if (address != null) {
            bean.setSalutations(titleFormFieldBeanFactory.createWithDefaultTitles(address.getTitle()));
            bean.setFirstName(address.getFirstName());
            bean.setLastName(address.getLastName());
            bean.setStreetName(address.getStreetName());
            bean.setAdditionalStreetInfo(address.getAdditionalStreetInfo());
            bean.setCity(address.getCity());
            bean.setPostalCode(address.getPostalCode());
            bean.setRegion(address.getRegion());
            bean.setCountries(countryFormFieldBeanFactory.create(availableCountries, address.getCountry()));
            bean.setPhone(address.getPhone());
            bean.setEmail(address.getEmail());
        } else {
            bean.setSalutations(titleFormFieldBeanFactory.createWithDefaultTitles(null));
            bean.setCountries(countryFormFieldBeanFactory.create(availableCountries, userContext.country()));
        }
    }
}
