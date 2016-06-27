package com.commercetools.sunrise.myaccount.addressbook;

import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;

public interface AddressFormData {

    void apply(@Nullable final Address address);

    Address toAddress();

    boolean isDefaultShippingAddress();

    boolean isDefaultBillingAddress();
}
