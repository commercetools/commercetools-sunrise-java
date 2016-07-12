package com.commercetools.sunrise.myaccount.authentication.login;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultLogInFormData extends Base implements LogInFormData {

    @Constraints.Required
    private String username;
    @Constraints.Required
    private String password;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}

