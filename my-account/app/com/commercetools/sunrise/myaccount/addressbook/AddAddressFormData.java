package com.commercetools.sunrise.myaccount.addressbook;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.commands.updateactions.AddAddress;

@ImplementedBy(DefaultAddAddressFormData.class)
public interface AddAddressFormData {

    AddAddress addAddress();

    boolean defaultShippingAddress();

    boolean defaultBillingAddress();
}
