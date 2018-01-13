package com.commercetools.sunrise.myaccount.addressbook;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.commands.updateactions.ChangeAddress;

@ImplementedBy(DefaultChangeAddressFormData.class)
public interface ChangeAddressFormData {

    ChangeAddress changeAddress();

    boolean defaultShippingAddress();

    boolean defaultBillingAddress();
}
