package com.commercetools.sunrise.myaccount.customerprofile;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.CustomerName;

@ImplementedBy(DefaultCustomerProfileFormData.class)
public interface CustomerProfileFormData {

    CustomerName customerName();

    String email();
}

