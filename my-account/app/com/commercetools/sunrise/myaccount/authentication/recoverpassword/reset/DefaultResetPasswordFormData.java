package com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset;

import play.data.validation.Constraints;

public class DefaultResetPasswordFormData implements ResetPasswordFormData {
    @Constraints.Required
    private String newPassword;

    @Constraints.Required
    private String confirmPassword;

    @Override
    public String newPassword() {
        return newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(final String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String validate() {
        final boolean isValid = newPassword.equals(confirmPassword) &&
                !newPassword.isEmpty();

        return isValid ? null : "Confirm password invalid";
    }
}
