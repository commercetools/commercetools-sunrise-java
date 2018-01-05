package com.commercetools.sunrise.models.customers;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.CustomerDraft;

@ImplementedBy(DefaultSignUpFormData.class)
public interface SignUpFormData {

    CustomerDraft customerDraft();
}

