package com.commercetools.sunrise.framework.components.controllers;

import com.commercetools.sunrise.common.localization.LocationSelectorControllerComponent;
import com.commercetools.sunrise.framework.viewmodels.header.PageNavMenuControllerComponent;
import com.commercetools.sunrise.sessions.customer.CustomerInSessionControllerComponent;

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
