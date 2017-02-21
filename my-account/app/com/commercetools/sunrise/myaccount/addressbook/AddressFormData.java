package com.commercetools.sunrise.myaccount.addressbook;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.models.Address;

@ImplementedBy(DefaultAddressFormData.class)
public interface AddressFormData {

    Address obtainAddress();

    boolean obtainIsDefaultShippingAddress();

    boolean obtainIsDefaultBillingAddress();

    void applyAddress(final Address address);

    void applyIsDefaultShippingAddress(final boolean defaultShippingAddress);

    void applyIsDefaultBillingAddress(final boolean defaultBillingAddress);
}
