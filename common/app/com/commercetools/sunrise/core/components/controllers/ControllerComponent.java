package com.commercetools.sunrise.core.components.controllers;

import com.commercetools.sunrise.core.components.SunriseComponent;

import java.util.Collections;
import java.util.List;

public interface ControllerComponent extends SunriseComponent, ControllerComponentSupplier {

    @Override
    default List<ControllerComponent> get() {
        return Collections.singletonList(this);
    }
}
