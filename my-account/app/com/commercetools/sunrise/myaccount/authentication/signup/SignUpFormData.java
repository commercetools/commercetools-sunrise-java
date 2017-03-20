package com.commercetools.sunrise.myaccount.authentication.signup;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.CustomerDraft;

@ImplementedBy(DefaultSignUpFormData.class)
public interface SignUpFormData {

    CustomerDraft customerDraft();
}

