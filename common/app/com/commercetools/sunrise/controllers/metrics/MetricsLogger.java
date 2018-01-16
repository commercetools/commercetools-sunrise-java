package com.commercetools.sunrise.controllers.metrics;

import com.commercetools.sdk.CtpLogUtils;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.metrics.ObservedTotalDuration;
import io.sphere.sdk.client.metrics.SimpleMetricsSphereClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.stream.Collectors.joining;

/**
 * Action that logs metrics for every request.
 */
final class MetricsLogger extends Action.Simple {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsLogger.class);

    private final SphereClient sphereClient;

    @Inject
    MetricsLogger(final SphereClient sphereClient) {
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<Result> call(final Http.Context ctx) {
        if (LOGGER.isDebugEnabled()) {
            if (sphereClient instanceof SimpleMetricsSphereClient) {
                return callWithMetrics((SimpleMetricsSphereClient) sphereClient, ctx);
            } else {
                LOGGER.warn(ctx.request() + " enabled logging via @LogMetrics annotation without a SimpleMetricsSphereClient");
            }
        }
        return delegate.call(ctx);
    }

    private CompletionStage<Result> callWithMetrics(final SimpleMetricsSphereClient metricsSphereClient, final Http.Context ctx) {
        final long startTimestamp = System.currentTimeMillis();
        final SphereClientMetricsObserver observer = new SphereClientMetricsObserver();
        metricsSphereClient.getMetricObservable().addObserver(observer);
        final CompletionStage<Result> resultStage = delegate.call(ctx);
        return resultStage.thenApply(result -> {
            final long stopTimestamp = System.currentTimeMillis();
            logRequestData(observer.getMetrics(), ctx, stopTimestamp - startTimestamp);
            metricsSphereClient.getMetricObservable().deleteObserver(observer);
            return result;
        });
    }

    private static void logRequestData(final List<ObservedTotalDuration> metrics, final Http.Context ctx, final long totalDuration) {
        String report = metrics.stream()
                .map(MetricsLogger::buildReport)
                .collect(joining("\n"));
        if (!report.isEmpty()) {
            report = ":\n" + report;
        }
        LOGGER.debug("({}ms) {} used {} CTP request(s){}",
                totalDuration,
                ctx.request(),
                metrics.size(),
                report);
    }

    private static String buildReport(final ObservedTotalDuration metric) {
        String request = String.format("(%dms) %s",
                metric.getDurationInMilliseconds(),
                CtpLogUtils.printableRequest(metric.getRequest()));
        if (!request.isEmpty() && LOGGER.isTraceEnabled()) {
            request += "\n with correlation ID: " + metric.getCorrelationId();
            request += "\n" + CtpLogUtils.printableResponse(metric.getSuccessResult());
        }
        return request;
    }
}