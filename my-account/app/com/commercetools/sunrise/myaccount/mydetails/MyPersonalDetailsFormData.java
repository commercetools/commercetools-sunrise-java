package com.commercetools.sunrise.myaccount.mydetails;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.CustomerName;

@ImplementedBy(DefaultMyPersonalDetailsFormData.class)
public interface MyPersonalDetailsFormData {

    CustomerName customerName();

    String email();

    void applyCustomerName(final CustomerName customerName);

    void applyEmail(final String email);
}

