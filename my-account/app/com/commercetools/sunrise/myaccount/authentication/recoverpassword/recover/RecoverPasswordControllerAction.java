package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.CustomerToken;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * This controller action is responsible for creating the password reset link from the
 * created {@link CustomerToken} and send it to the customers email address.
 */
@ImplementedBy(DefaultRecoverPasswordControllerAction.class)
@FunctionalInterface
public interface RecoverPasswordControllerAction extends ControllerAction, Function<RecoverPasswordFormData, CompletionStage<CustomerToken>> {

    @Override
    CompletionStage<CustomerToken> apply(RecoverPasswordFormData recoveryEmailFormData);
}
