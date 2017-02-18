package com.commercetools.sunrise.hooks;

import com.commercetools.sunrise.framework.ControllerComponentSupplier;
import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@With(RegisterComponentsAction.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisteredComponents {
    Class<? extends ControllerComponentSupplier>[] value();
}
