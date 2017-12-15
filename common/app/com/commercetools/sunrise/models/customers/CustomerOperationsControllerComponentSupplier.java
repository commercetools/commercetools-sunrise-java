package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.components.controllers.AbstractControllerComponentSupplier;

import javax.inject.Inject;

public class CustomerOperationsControllerComponentSupplier extends AbstractControllerComponentSupplier {

    @Inject
    public CustomerOperationsControllerComponentSupplier(final CustomerInSessionControllerComponent customerInSessionControllerComponent) {
        add(customerInSessionControllerComponent);
    }
}
