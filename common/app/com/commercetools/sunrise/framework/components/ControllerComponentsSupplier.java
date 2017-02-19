package com.commercetools.sunrise.framework.components;

import java.util.List;
import java.util.function.Supplier;

@FunctionalInterface
public interface ControllerComponentsSupplier extends Supplier<List<ControllerComponent>> {

}
