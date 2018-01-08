package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(DefaultChangePasswordControllerAction.class)
@FunctionalInterface
public interface ChangePasswordControllerAction extends ControllerAction, Function<ChangePasswordFormData, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final ChangePasswordFormData formData);
}
