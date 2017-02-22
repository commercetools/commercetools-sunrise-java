package com.commercetools.sunrise.framework.controllers.metrics;

import com.commercetools.sunrise.common.utils.LogUtils;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.metrics.ObservedTotalDuration;
import io.sphere.sdk.client.metrics.SimpleMetricsSphereClient;
import io.sphere.sdk.models.Base;
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
        if (sphereClient instanceof SimpleMetricsSphereClient) {
            return callWithMetrics((SimpleMetricsSphereClient) sphereClient, ctx);
        } else {
            LOGGER.warn("Using @LogMetrics annotation without a SimpleMetricsSphereClient");
            return delegate.call(ctx);
        }
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
        String reportPerRequest = printableReportPerRequest(metrics);
        if (!reportPerRequest.isEmpty()) {
            reportPerRequest = ":\n" + reportPerRequest;
        }
        LOGGER.debug("({}ms) {} used {} CTP request(s){}",
                totalDuration,
                ctx.request(),
                metrics.size(),
                reportPerRequest);
    }

    private static String printableReportPerRequest(final List<ObservedTotalDuration> metrics) {
        if (LOGGER.isTraceEnabled()) {
            return metrics.stream()
                    .map(Base::toString)
                    .collect(joining("\n"));
        } else {
            return metrics.stream()
                    .map(data -> String.format("(%dms) %s",
                            data.getDurationInMilliseconds(),
                            LogUtils.printableRequest(data.getRequest())))
                    .collect(joining("\n"));
        }
    }
}