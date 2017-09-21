package com.commercetools.sunrise.framework.components.controllers;

import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@With(ComponentsRegisterer.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisteredComponents {
    Class<? extends ControllerComponentSupplier>[] value();
}
