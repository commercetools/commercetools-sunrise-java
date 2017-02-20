package com.commercetools.sunrise.framework.controllers.metrics;

import io.sphere.sdk.client.metrics.SimpleMetricsSphereClient;
import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Apply on a controller or a controller action, together with the {@link SimpleMetricsSphereClient}.
 * Enabling the {@code DEBUG} mode on the Logger {@link MetricsLogger} will log a report based on the request,
 * containing information such as the duration and some metrics on each of the requests done by the {@code SphereClient}.
 */
@With(MetricsLogger.class)
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMetrics {
}