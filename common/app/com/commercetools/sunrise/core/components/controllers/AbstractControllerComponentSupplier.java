package com.commercetools.sunrise.core.components.controllers;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractControllerComponentSupplier implements ControllerComponentSupplier {

    private final List<ControllerComponent> components = new ArrayList<>();

    protected void add(final ControllerComponentSupplier supplier) {
        components.addAll(supplier.get());
    }

    @Override
    public List<ControllerComponent> get() {
        return components;
    }
}
