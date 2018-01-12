package com.commercetools.sunrise.myaccount.authentication.resetpassword;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;

/**
 * This action performs the customer password reset.
 */
@ImplementedBy(DefaultResetPasswordFormAction.class)
public interface ResetPasswordFormAction extends FormAction<ResetPasswordFormData> {

}
