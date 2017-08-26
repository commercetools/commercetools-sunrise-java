package com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

/**
 * This action performs the customer password reset.
 */
@ImplementedBy(DefaultResetPasswordControllerAction.class)
@FunctionalInterface
public interface ResetPasswordControllerAction extends ControllerAction, BiFunction<String, ResetPasswordFormData, CompletionStage<Customer>> {

    /**
     * Performs a customer password reset.
     *
     * @param resetToken            the reset token that a customer received via a recovery email
     * @param resetPasswordFormData the reset password form data
     * @return the customer
     */
    @Override
    CompletionStage<Customer> apply(String resetToken, ResetPasswordFormData resetPasswordFormData);
}
