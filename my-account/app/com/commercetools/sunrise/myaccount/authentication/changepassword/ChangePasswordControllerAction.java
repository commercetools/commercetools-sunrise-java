package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultChangePasswordControllerAction.class)
public interface ChangePasswordControllerAction extends ControllerAction<ChangePasswordFormData> {

}
