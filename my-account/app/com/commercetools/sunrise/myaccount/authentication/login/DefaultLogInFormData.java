package com.commercetools.sunrise.myaccount.authentication.login;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.Required;

public class DefaultLogInFormData extends Base implements LogInFormData {

    @Required
    private String username;
    @Required
    private String password;

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public void applyUsername(final String username) {
        this.username = username;
    }


    // Getters & setters

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}

