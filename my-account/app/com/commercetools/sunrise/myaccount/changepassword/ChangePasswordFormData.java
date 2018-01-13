package com.commercetools.sunrise.myaccount.changepassword;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultChangePasswordFormData.class)
public interface ChangePasswordFormData {

    String oldPassword();

    String newPassword();
}
