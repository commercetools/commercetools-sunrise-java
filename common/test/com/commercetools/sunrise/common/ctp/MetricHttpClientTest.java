package com.commercetools.sunrise.common.ctp;

import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.http.HttpMethod;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.HttpResponse;
import org.junit.Test;
import play.mvc.Http;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

public class MetricHttpClientTest {
    private static final HttpRequest SOME_REQUEST = HttpRequest.of(HttpMethod.GET, "somewhere");
    private static final HttpResponse SOME_RESPONSE = HttpResponse.of(200);

// TODO Do we still need this?
//    @Test
//    public void executesRequestWithNoContext() throws Exception {
//        executeWithoutContext(SOME_REQUEST, response -> assertThat(response).isEqualTo(SOME_RESPONSE));
//    }

    @Test
    public void executesRequest() throws Exception {
        testMetrics(SOME_REQUEST, (response, context) -> assertThat(response).isEqualTo(SOME_RESPONSE));
    }

    @Test
    public void reportsMetrics() throws Exception {
        testMetrics(SOME_REQUEST, (response, context) -> {
            final ReportRawData report = reportFromContext(context).get(0);
            assertThat(report.getHttpRequest()).isEqualTo(SOME_REQUEST);
            assertThat(report.getHttpResponse()).isEqualTo(SOME_RESPONSE);
            assertThat(report.getStartTimestamp()).isLessThanOrEqualTo(report.getStopTimestamp());
        });
    }

    private void testMetrics(final HttpRequest request, BiConsumer<HttpResponse, Http.Context> test) {
        final Logger metricsLogger = (Logger) LoggerFactory.getLogger(MetricHttpClient.LOGGER_NAME);
        metricsLogger.setLevel(Level.DEBUG); // TODO check better way to do this
        final Http.Context context = emptyContext();
        final MetricHttpClient metricHttpClient = MetricHttpClient.of(httpClient(), context);
        final HttpResponse response = metricHttpClient.execute(request).toCompletableFuture().join();
        test.accept(response, context);
        metricsLogger.setLevel(Level.OFF);
    }

    @SuppressWarnings("unchecked")
    private List<ReportRawData> reportFromContext(final Http.Context context) {
        return (List<ReportRawData>) context.args.get(MetricAction.KEY);
    }

    private Http.Context emptyContext() {
        final Http.Context context = new Http.Context(null, null, null, emptyMap(), emptyMap(), emptyMap());
        context.args.put(MetricAction.KEY, new LinkedList<>());
        return context;
    }

    private HttpClient httpClient() {
        return new HttpClient() {
                @Override
                public CompletionStage<HttpResponse> execute(final HttpRequest httpRequest) {
                    return CompletableFuture.completedFuture(SOME_RESPONSE);
                }

                @Override
                public void close() {
                }
            };
    }
}
