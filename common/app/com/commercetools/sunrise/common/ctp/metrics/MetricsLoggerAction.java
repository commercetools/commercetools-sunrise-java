package com.commercetools.sunrise.common.ctp.metrics;

import com.commercetools.sunrise.common.utils.LogUtils;
import com.google.inject.Inject;
import io.sphere.sdk.client.metrics.ObservedTotalDuration;
import io.sphere.sdk.client.metrics.SimpleMetricsSphereClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.stream.Collectors.joining;

/**
 * Action that logs metrics for every request.
 */
final class MetricsLoggerAction extends Action.Simple {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsLoggerAction.class);

    private SimpleMetricsSphereClient metricsSphereClient;

    @Inject(optional = true)
    void setSphereClient(final SimpleMetricsSphereClient metricsSphereClient) {
        this.metricsSphereClient = metricsSphereClient;
    }

    @Override
    public CompletionStage<Result> call(final Http.Context ctx) {
        if (metricsSphereClient != null) {
            final long startTimestamp = System.currentTimeMillis();
            final SphereClientMetricsObserver observer = new SphereClientMetricsObserver();
            metricsSphereClient.getMetricObservable().addObserver(observer);
            final CompletionStage<Result> result = delegate.call(ctx);
            result.thenAccept(res -> {
                final long stopTimestamp = System.currentTimeMillis();
                logRequestData(observer.getMetrics(), ctx, stopTimestamp - startTimestamp);
                metricsSphereClient.getMetricObservable().deleteObserver(observer);
            });
            return result;
        } else {
            LOGGER.warn("Using @LogMetrics annotation without a SimpleMetricsSphereClient");
            return delegate.call(ctx);
        }
    }

    private static void logRequestData(final List<ObservedTotalDuration> metrics, final Http.Context ctx, final long totalDuration) {
        if (!metrics.isEmpty()) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("CTP metrics: {}", metrics);
            } else {
                final String reportPerRequest = printableReportPerRequest(metrics);
                LOGGER.debug("({}ms) {} used {} request(s):\n{}",
                        totalDuration,
                        ctx.request(),
                        metrics.size(),
                        reportPerRequest);
            }
        }
    }

    private static String printableReportPerRequest(final List<ObservedTotalDuration> metrics) {
        return metrics.stream()
                .map(data -> String.format("(%dms) %s",
                        data.getDurationInMilliseconds(),
                        LogUtils.printableRequest(data.getRequest())))
                .collect(joining("\n"));
    }
}