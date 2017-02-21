package com.commercetools.sunrise.myaccount.authentication.login;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultLogInFormData.class)
public interface LogInFormData {

    String obtainUsername();

    String obtainPassword();

    void applyUsername(final String username);
}

