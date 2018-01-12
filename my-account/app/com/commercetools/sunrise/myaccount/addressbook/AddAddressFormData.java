package com.commercetools.sunrise.myaccount.addressbook;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.models.Address;

@ImplementedBy(DefaultAddAddressFormData.class)
public interface AddAddressFormData {

    Address address();

    boolean defaultShippingAddress();

    boolean defaultBillingAddress();
}
