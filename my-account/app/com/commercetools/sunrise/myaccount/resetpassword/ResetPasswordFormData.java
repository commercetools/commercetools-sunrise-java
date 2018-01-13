package com.commercetools.sunrise.myaccount.resetpassword;

import com.google.inject.ImplementedBy;

/**
 * The data required to reset a customers password.
 */
@ImplementedBy(DefaultResetPasswordFormData.class)
public interface ResetPasswordFormData {

    String resetToken();

    String newPassword();
}
