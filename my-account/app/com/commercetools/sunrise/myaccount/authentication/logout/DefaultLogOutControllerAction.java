package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.models.carts.MyCartInSession;
import com.commercetools.sunrise.models.customers.CustomerInSession;

import javax.inject.Inject;

public class DefaultLogOutControllerAction implements LogOutControllerAction {

    private final CustomerInSession customerInSession;
    private final MyCartInSession myCartInSession;

    @Inject
    protected DefaultLogOutControllerAction(final CustomerInSession customerInSession, final MyCartInSession myCartInSession) {
        this.customerInSession = customerInSession;
        this.myCartInSession = myCartInSession;
    }

    @Override
    public void run() {
        customerInSession.remove();
        myCartInSession.remove();
    }
}
