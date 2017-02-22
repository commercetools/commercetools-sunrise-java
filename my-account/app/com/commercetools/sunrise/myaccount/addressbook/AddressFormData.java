package com.commercetools.sunrise.myaccount.addressbook;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.models.Address;

@ImplementedBy(DefaultAddressFormData.class)
public interface AddressFormData {

    Address address();

    boolean defaultShippingAddress();

    boolean defaultBillingAddress();

    void applyAddress(final Address address);

    void applyDefaultShippingAddress(final boolean defaultShippingAddress);

    void applyDefaultBillingAddress(final boolean defaultBillingAddress);
}
