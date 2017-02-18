package com.commercetools.sunrise.common;

import com.commercetools.sunrise.common.reverserouter.TemplateLinksComponentSupplier;
import com.commercetools.sunrise.common.sessions.cart.CartInSessionControllerComponent;
import com.commercetools.sunrise.common.sessions.customer.CustomerInSessionControllerComponent;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.ControllerComponentSupplier;
import com.commercetools.sunrise.shoppingcart.CartFieldsUpdaterControllerComponent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CommonControllerComponentSupplier implements ControllerComponentSupplier {

    private final List<ControllerComponent> components = new ArrayList<>();

    @Inject
    public CommonControllerComponentSupplier(final TemplateLinksComponentSupplier templateLinksComponentSupplier,
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
