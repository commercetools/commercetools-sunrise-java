package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.components.Component;
import com.commercetools.sunrise.core.components.ComponentSupplier;

import java.util.List;

import static java.util.Collections.singletonList;

public final class CustomerComponentSupplier implements ComponentSupplier {

    public static List<Class<? extends Component>> get() {
        return singletonList(CustomerStoringComponent.class);
    }
}
