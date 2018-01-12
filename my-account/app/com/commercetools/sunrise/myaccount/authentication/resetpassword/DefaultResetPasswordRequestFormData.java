package com.commercetools.sunrise.myaccount.authentication.resetpassword;


import play.data.validation.Constraints;

/**
 * Default implementation of {@link ResetPasswordRequestFormData}
 */
public class DefaultResetPasswordRequestFormData implements ResetPasswordRequestFormData {

    @Constraints.Required
    @Constraints.Email
    private String email;

    @Override
    public String email() {
        return email;
    }
}
