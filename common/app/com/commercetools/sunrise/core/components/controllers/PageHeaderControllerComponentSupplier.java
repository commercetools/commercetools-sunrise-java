package com.commercetools.sunrise.core.components.controllers;

import com.commercetools.sunrise.common.localization.LocationSelectorControllerComponent;
import com.commercetools.sunrise.core.viewmodels.header.PageNavMenuControllerComponent;
import com.commercetools.sunrise.models.carts.MiniCartControllerComponent;
import com.commercetools.sunrise.models.customers.CustomerInSessionControllerComponent;

import javax.inject.Inject;

public class PageHeaderControllerComponentSupplier extends AbstractControllerComponentSupplier {

    @Inject
    public PageHeaderControllerComponentSupplier(final MiniCartControllerComponent miniCartControllerComponent,
                                                 final PageNavMenuControllerComponent pageNavMenuControllerComponent,
                                                 final LocationSelectorControllerComponent locationSelectorControllerComponent,
                                                 final CustomerInSessionControllerComponent customerInSessionControllerComponent) {
        add(miniCartControllerComponent);
        add(pageNavMenuControllerComponent);
        add(locationSelectorControllerComponent);
        add(customerInSessionControllerComponent);
    }
}
