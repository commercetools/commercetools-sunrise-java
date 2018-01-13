package com.commercetools.sunrise.myaccount.addressbook;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.commands.updateactions.RemoveAddress;

@ImplementedBy(DefaultRemoveAddressFormData.class)
public interface RemoveAddressFormData {

    RemoveAddress removeAddress();
}
