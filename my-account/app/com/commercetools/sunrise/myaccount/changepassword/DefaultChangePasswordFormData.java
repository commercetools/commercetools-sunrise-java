package com.commercetools.sunrise.myaccount.changepassword;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultChangePasswordFormData extends Base implements ChangePasswordFormData {

    @Constraints.Required
    private String oldPassword;

    @Constraints.Required
    private String newPassword;

    @Constraints.Required
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
        if (!newPassword.equals(repeatPassword)) {
            return "errors.notMatchingPasswords";
        }
        return null;
    }
}

