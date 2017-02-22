package com.commercetools.sunrise.myaccount.authentication.login;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultLogInFormData.class)
public interface LogInFormData {

    String username();

    String password();

    void applyUsername(final String username);
}

