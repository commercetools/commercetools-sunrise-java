package com.commercetools.sunrise.myaccount.resetpassword;

import play.data.validation.Constraints;

public class DefaultResetPasswordFormData implements ResetPasswordFormData {

    @Constraints.Required
    private String resetToken;

    @Constraints.MinLength(4)
    @Constraints.Required
    private String newPassword;

    @Constraints.Required
    private String confirmPassword;

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
