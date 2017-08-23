package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import play.data.validation.Constraints;

/**
 * Default implementation of {@link RecoverPasswordFormData}
 */
public class DefaultRecoverPasswordFormData implements RecoverPasswordFormData {
    @Constraints.Required
    @Constraints.Email
    private String email;

    @Override
    public String email() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
