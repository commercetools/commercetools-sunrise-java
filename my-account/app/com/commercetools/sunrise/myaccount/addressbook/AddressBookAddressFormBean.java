package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.AddressFormBean;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Address;
import play.Configuration;

import javax.annotation.Nullable;
import java.util.List;

public class AddressBookAddressFormBean extends AddressFormBean {

    private boolean defaultShippingAddress;
    private boolean defaultBillingAddress;

    public AddressBookAddressFormBean() {
    }

    public AddressBookAddressFormBean(@Nullable final Address address, final List<CountryCode> availableCountries,
                                      final UserContext userContext, final I18nResolver i18nResolver, final Configuration configuration) {
        super(address, availableCountries, userContext, i18nResolver, configuration);
    }

    public boolean isDefaultShippingAddress() {
        return defaultShippingAddress;
    }

    public void setDefaultShippingAddress(final boolean defaultShippingAddress) {
        this.defaultShippingAddress = defaultShippingAddress;
    }

    public boolean isDefaultBillingAddress() {
        return defaultBillingAddress;
    }

    public void setDefaultBillingAddress(final boolean defaultBillingAddress) {
        this.defaultBillingAddress = defaultBillingAddress;
    }
}
