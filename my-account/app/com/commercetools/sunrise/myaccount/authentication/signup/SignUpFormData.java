package com.commercetools.sunrise.myaccount.authentication.signup;

import io.sphere.sdk.customers.CustomerDraftBuilder;

public interface SignUpFormData {

    CustomerDraftBuilder toCustomerDraftBuilder();
}

