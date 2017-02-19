package com.commercetools.sunrise.myaccount.addressbook;

import io.sphere.sdk.models.Address;

public interface AddressFormData {

    Address toAddress();

    void applyAddress(final Address address);
    
    boolean isDefaultShippingAddress();

    boolean isDefaultBillingAddress();

    void setDefaultShippingAddress(final boolean defaultShippingAddress);

    void setDefaultBillingAddress(final boolean defaultBillingAddress);
}
