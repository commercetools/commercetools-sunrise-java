package com.commercetools.sunrise.myaccount.authentication.login;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.CustomerSignInResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(DefaultLogInFunction.class)
@FunctionalInterface
public interface LogInFunction extends Function<LogInFormData, CompletionStage<CustomerSignInResult>> {

    @Override
    CompletionStage<CustomerSignInResult> apply(final LogInFormData formData);
}
