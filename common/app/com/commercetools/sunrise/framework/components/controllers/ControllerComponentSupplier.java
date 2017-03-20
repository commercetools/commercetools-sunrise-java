package com.commercetools.sunrise.framework.components.controllers;

import java.util.List;
import java.util.function.Supplier;

@FunctionalInterface
public interface ControllerComponentSupplier extends Supplier<List<ControllerComponent>> {

}
