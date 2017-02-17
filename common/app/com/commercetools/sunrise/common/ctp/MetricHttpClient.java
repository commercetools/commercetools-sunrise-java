package com.commercetools.sunrise.common.ctp;

import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

final class MetricHttpClient implements HttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricHttpClient.class);
    static final String KEY = "io.sphere.sdk.play.metrics.reportRawData";

    private final HttpClient underlying;
    private final Http.Context context;
    private final boolean metricsEnabled;

    private MetricHttpClient(final HttpClient underlying, final Http.Context context) {
        this.underlying = underlying;
        this.context = context;
        this.metricsEnabled = LOGGER.isDebugEnabled();
        if (metricsEnabled) {
            final List<ReportRawData> rawData = Collections.synchronizedList(new LinkedList<>());
            context.args.put(KEY, rawData);
        }
    }

    @Override
    public CompletionStage<HttpResponse> execute(final HttpRequest httpRequest) {
        final long startTimestamp = System.currentTimeMillis();
        return underlying.execute(httpRequest).thenApply(res -> {//important to not use async here
            final long stopTimestamp = System.currentTimeMillis();
            if (metricsEnabled) {
                report(context, httpRequest, res, startTimestamp, stopTimestamp);
            }
            return res;
        });
    }

    @SuppressWarnings("unchecked")
    private void report(final Http.Context context, final HttpRequest httpRequest, final HttpResponse response, long startTimestamp, long stopTimestamp) {
        final Optional<List<ReportRawData>> dataOptional = Optional.ofNullable((List<ReportRawData>) context.args.get(KEY));
        dataOptional.ifPresent(data -> data.add(new ReportRawData(httpRequest, response, startTimestamp, stopTimestamp)));
    }

    @Override
    public void close() {
        underlying.close();
    }

    public static MetricHttpClient of(final HttpClient underlying, final Http.Context context) {
        return new MetricHttpClient(underlying, context);
    }
}