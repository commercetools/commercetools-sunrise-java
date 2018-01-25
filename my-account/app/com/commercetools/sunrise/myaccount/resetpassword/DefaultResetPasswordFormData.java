package com.commercetools.sunrise.myaccount.resetpassword;

import play.data.validation.Constraints;

public class DefaultResetPasswordFormData implements ResetPasswordFormData {

    @Constraints.Required
    public String resetToken;

    @Constraints.MinLength(4)
    @Constraints.Required
    public String newPassword;

    @Constraints.Required
    public String confirmPassword;

    @Override
    public String resetToken() {
        return resetToken;
    }

    @Override
    public String newPassword() {
        return newPassword;
    }

    public String validate() {
        if (!newPassword.equals(confirmPassword)) {
            return "errors.notMatchingPasswords";
        }
        return null;
    }
}
