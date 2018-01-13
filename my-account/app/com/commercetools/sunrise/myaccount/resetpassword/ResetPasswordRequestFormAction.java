package com.commercetools.sunrise.myaccount.resetpassword;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.CustomerToken;

/**
 * This controller action is responsible for creating the password reset link from the
 * created {@link CustomerToken} and send it to the customers email address.
 */
@ImplementedBy(DefaultResetPasswordRequestFormAction.class)
@FunctionalInterface
public interface ResetPasswordRequestFormAction extends FormAction<ResetPasswordRequestFormData> {

}
