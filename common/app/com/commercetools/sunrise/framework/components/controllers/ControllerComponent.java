package com.commercetools.sunrise.framework.components.controllers;

import com.commercetools.sunrise.framework.components.SunriseComponent;

import java.util.Collections;
import java.util.List;

public interface ControllerComponent extends SunriseComponent, ControllerComponentSupplier {

    @Override
    default List<ControllerComponent> get() {
        return Collections.singletonList(this);
    }
}
