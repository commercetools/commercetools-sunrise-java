package com.commercetools.sunrise.framework.annotations;

import com.commercetools.sunrise.framework.ControllerComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IntroducingMultiControllerComponents {
    Class<? extends ControllerComponent>[] value();
}
