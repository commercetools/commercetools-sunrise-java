package com.commercetools.sunrise.myaccount.authentication;

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
}

