package com.commercetools.sunrise.framework.hooks;

import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Place on every controller action method of your project where you want to enable the hook functionality.
 * Without this annotation some hooks may not properly function or not be enabled at all.
 */
@With(HooksEnabler.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableHooks {
}
