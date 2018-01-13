package com.commercetools.sunrise.myaccount.resetpassword;

import com.commercetools.sunrise.core.FormAction;
import com.google.inject.ImplementedBy;

/**
 * This action performs the customer password reset.
 */
@ImplementedBy(DefaultResetPasswordFormAction.class)
public interface ResetPasswordFormAction extends FormAction<ResetPasswordFormData> {

}
