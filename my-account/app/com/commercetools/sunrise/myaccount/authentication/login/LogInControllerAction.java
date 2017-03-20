package com.commercetools.sunrise.myaccount.authentication.login;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.CustomerSignInResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(DefaultLogInControllerAction.class)
@FunctionalInterface
public interface LogInControllerAction extends ControllerAction, Function<LogInFormData, CompletionStage<CustomerSignInResult>> {

    @Override
    CompletionStage<CustomerSignInResult> apply(final LogInFormData formData);
}
