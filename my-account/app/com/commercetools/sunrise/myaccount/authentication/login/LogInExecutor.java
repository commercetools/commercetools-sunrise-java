package com.commercetools.sunrise.myaccount.authentication.login;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.CustomerSignInResult;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultLogInExecutor.class)
public interface LogInExecutor {

    CompletionStage<CustomerSignInResult> logIn(final LogInFormData formData);
}
