package com.commercetools.sunrise.myaccount.addressbook;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultRemoveAddressFormData.class)
public interface RemoveAddressFormData {

    String addressId();
}
