package com.commercetools.sunrise.sessions.cart;

import com.commercetools.sunrise.framework.components.controllers.AbstractControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.CartFieldsUpdaterControllerComponent;

import javax.inject.Inject;

public class CartOperationsControllerComponentSupplier extends AbstractControllerComponentSupplier {

    @Inject
    public CartOperationsControllerComponentSupplier(final CartFieldsUpdaterControllerComponent cartFieldsUpdaterControllerComponent,
                                                     final CartInSessionControllerComponent cartInSessionControllerComponent) {
        add(cartFieldsUpdaterControllerComponent);
        add(cartInSessionControllerComponent);
    }
}
