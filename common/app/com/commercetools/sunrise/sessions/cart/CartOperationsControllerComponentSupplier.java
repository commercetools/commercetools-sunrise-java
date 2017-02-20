package com.commercetools.sunrise.sessions.cart;

import com.commercetools.sunrise.framework.components.AbstractControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.CartFieldsUpdaterControllerComponent;

import javax.inject.Inject;

public class CartOperationsControllerComponentSupplier extends AbstractControllerComponentSupplier {

    @Inject
    public CartOperationsControllerComponentSupplier(final CartFieldsUpdaterControllerComponent cartFieldsUpdaterControllerComponent,
                                                     final CartInSessionControllerComponent cartInSessionControllerComponent) {
        add(cartFieldsUpdaterControllerComponent);
        add(cartInSessionControllerComponent);
    }
}
