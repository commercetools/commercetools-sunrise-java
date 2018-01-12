package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultLogInControllerAction.class)
@FunctionalInterface
public interface LogInControllerAction extends ControllerAction {

}
