package com.commercetools.sunrise.framework;

import java.util.List;
import java.util.function.Supplier;

@FunctionalInterface
public interface ControllerComponentListSupplier extends Supplier<List<ControllerComponent>> {

}
