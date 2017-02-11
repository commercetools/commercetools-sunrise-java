package com.commercetools.sunrise.myaccount.authentication.signup;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.CustomerSignInResult;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCustomerCreator.class)
public interface CustomerCreator {

    CompletionStage<CustomerSignInResult> createCustomer(final SignUpFormData formData);
}
