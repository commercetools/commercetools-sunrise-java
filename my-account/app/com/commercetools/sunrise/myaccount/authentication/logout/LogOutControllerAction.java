package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultLogOutControllerAction.class)
@FunctionalInterface
public interface LogOutControllerAction extends ControllerAction, Runnable {

}
