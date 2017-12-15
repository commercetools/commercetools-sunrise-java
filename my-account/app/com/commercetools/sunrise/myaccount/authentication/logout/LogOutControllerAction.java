package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultLogOutControllerAction.class)
@FunctionalInterface
public interface LogOutControllerAction extends ControllerAction, Runnable {

}
