package com.commercetools.sunrise.myaccount.authentication;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultLogInFormData.class)
public interface LogInFormData {

    String username();

    String password();
}

