package com.commercetools.sunrise.framework.reverserouters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Place on a controller action method of your project, together with a route tag,
 * if you want to change a pre-defined route in Sunrise framework.
 *
 * For example, if you would like to create your one Product Detail Controller not based on
 * {@code SunriseProductDetailController} you would add on your own action method the annotation:
 * {@code @SunriseRoute(ProductReverseRouter.PRODUCT_DETAIL_PAGE)}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SunriseRoute {
    String[] value();
}
