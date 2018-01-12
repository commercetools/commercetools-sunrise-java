package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultSignUpControllerAction.class)
@FunctionalInterface
public interface SignUpControllerAction extends ControllerAction {

}
