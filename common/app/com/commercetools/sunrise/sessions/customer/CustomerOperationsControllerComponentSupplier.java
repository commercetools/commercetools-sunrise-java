package com.commercetools.sunrise.sessions.customer;

import com.commercetools.sunrise.framework.components.controllers.AbstractControllerComponentSupplier;

import javax.inject.Inject;

public class CustomerOperationsControllerComponentSupplier extends AbstractControllerComponentSupplier {

    @Inject
    public CustomerOperationsControllerComponentSupplier(final CustomerInSessionControllerComponent customerInSessionControllerComponent) {
        add(customerInSessionControllerComponent);
    }
}
