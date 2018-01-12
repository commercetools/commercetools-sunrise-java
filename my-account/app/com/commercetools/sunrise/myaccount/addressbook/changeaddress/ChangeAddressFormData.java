package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.models.Address;

@ImplementedBy(DefaultChangeAddressFormData.class)
public interface ChangeAddressFormData {

    String addressId();

    Address address();

    boolean defaultShippingAddress();

    boolean defaultBillingAddress();
}
