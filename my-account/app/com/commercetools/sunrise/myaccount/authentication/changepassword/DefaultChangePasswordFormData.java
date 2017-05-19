package com.commercetools.sunrise.myaccount.authentication.changepassword;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.Required;

public class DefaultChangePasswordFormData extends Base implements ChangePasswordFormData {

    @Required
    private String oldPassword;
    @Required
    private String newPassword;
    @Required
    private String repeatPassword;

    @Override
    public String oldPassword() {
        return oldPassword;
    }

    @Override
    public String newPassword() {
        return newPassword;
    }

    public String validate() {
        if (newPassword == null || repeatPassword == null || !newPassword.equals(repeatPassword)) {
            return "New password does not match confirmed one"; // TODO use i18n version
        }
        return null;
    }

    // Getters & setters

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(final String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(final String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}

