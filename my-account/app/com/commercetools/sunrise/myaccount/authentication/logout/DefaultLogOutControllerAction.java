package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.sessions.cart.CartInSession;
import com.commercetools.sunrise.sessions.customer.CustomerInSession;

import javax.inject.Inject;

public class DefaultLogOutControllerAction implements LogOutControllerAction {

    private final CustomerInSession customerInSession;
    private final CartInSession cartInSession;

    @Inject
    protected DefaultLogOutControllerAction(final CustomerInSession customerInSession, final CartInSession cartInSession) {
        this.customerInSession = customerInSession;
        this.cartInSession = cartInSession;
    }

    @Override
    public void run() {
        customerInSession.remove();
        cartInSession.remove();
    }
}
