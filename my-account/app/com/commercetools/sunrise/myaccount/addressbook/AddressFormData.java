package com.commercetools.sunrise.myaccount.addressbook;

import io.sphere.sdk.models.Address;

public interface AddressFormData {

    Address toAddress();

    boolean isDefaultShippingAddress();

    boolean isDefaultBillingAddress();
}
