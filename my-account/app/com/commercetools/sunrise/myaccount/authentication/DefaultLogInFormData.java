package com.commercetools.sunrise.myaccount.authentication;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultLogInFormData extends Base implements LogInFormData {

    @Constraints.Required
    public String username;

    @Constraints.MinLength(1)
    @Constraints.Required
    public String password;

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }
}

