package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultChangePasswordControllerAction.class)
@FunctionalInterface
public interface ChangePasswordControllerAction extends ControllerAction, BiFunction<Customer, ChangePasswordFormData, CompletionStage<Customer>> {

    @Override
    CompletionStage<Customer> apply(final Customer customer, final ChangePasswordFormData formData);
}
