package com.commercetools.sunrise.core.components.controllers;

import com.commercetools.sunrise.models.carts.MiniCartControllerComponent;
import com.commercetools.sunrise.models.customers.CustomerInSessionControllerComponent;

import javax.inject.Inject;

public class PageHeaderControllerComponentSupplier extends AbstractControllerComponentSupplier {

    @Inject
    public PageHeaderControllerComponentSupplier(final MiniCartControllerComponent miniCartControllerComponent,
                                                 final CustomerInSessionControllerComponent customerInSessionControllerComponent) {
        add(miniCartControllerComponent);
        add(customerInSessionControllerComponent);
    }
}
