package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.core.FormAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultLogInFormAction.class)
@FunctionalInterface
public interface LogInFormAction extends FormAction<LogInFormData> {

}
