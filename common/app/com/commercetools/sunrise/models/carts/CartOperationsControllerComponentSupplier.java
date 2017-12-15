package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.controllers.AbstractControllerComponentSupplier;

import javax.inject.Inject;

public class CartOperationsControllerComponentSupplier extends AbstractControllerComponentSupplier {

    @Inject
    public CartOperationsControllerComponentSupplier(final CartFieldsUpdaterControllerComponent cartFieldsUpdaterControllerComponent,
                                                     final CartInSessionControllerComponent cartInSessionControllerComponent) {
        add(cartFieldsUpdaterControllerComponent);
        add(cartInSessionControllerComponent);
    }
}
