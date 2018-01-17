package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.Component;
import com.commercetools.sunrise.core.components.ComponentSupplier;

import java.util.List;

import static java.util.Arrays.asList;

public final class CartComponentSupplier implements ComponentSupplier {

    public static List<Class<? extends Component>> get() {
        return asList(
                MyCartComponent.class,
                CartFieldsUpdaterComponent.class,
                CartDiscountCodesComponent.class);
    }

}
