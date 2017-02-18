package com.commercetools.sunrise.common;

import com.commercetools.sunrise.framework.reverserouters.TemplateLinksComponentsSupplier;
import com.commercetools.sunrise.sessions.cart.CartInSessionControllerComponent;
import com.commercetools.sunrise.sessions.customer.CustomerInSessionControllerComponent;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.ControllerComponentsSupplier;
import com.commercetools.sunrise.shoppingcart.CartFieldsUpdaterControllerComponent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CommonControllerComponentsSupplier implements ControllerComponentsSupplier {

    private final List<ControllerComponent> components = new ArrayList<>();

    @Inject
    public CommonControllerComponentsSupplier(final TemplateLinksComponentsSupplier templateLinksComponentSupplier,
                                              final CartFieldsUpdaterControllerComponent cartFieldsUpdaterControllerComponent,
                                              final CartInSessionControllerComponent cartInSessionControllerComponent,
                                              final CustomerInSessionControllerComponent customerInSessionControllerComponent) {
        components.add(cartFieldsUpdaterControllerComponent);
        components.add(cartInSessionControllerComponent);
        components.add(customerInSessionControllerComponent);
        components.addAll(templateLinksComponentSupplier.get());
    }

    @Override
    public List<ControllerComponent> get() {
        return components;
    }
}
