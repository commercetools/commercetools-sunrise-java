package io.sphere.sdk.play.metrics;

import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.http.HttpMethod;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.HttpResponse;
import org.junit.Test;
import play.mvc.Http;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

import static io.sphere.sdk.play.metrics.MetricAction.KEY;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

public class MetricHttpClientTest {
    private static final HttpRequest SOME_REQUEST = HttpRequest.of(HttpMethod.GET, "somewhere");
    private static final HttpResponse SOME_RESPONSE = HttpResponse.of(200);

    @Test
    public void executesRequestWithNoContext() throws Exception {
        executeWithoutContext(SOME_REQUEST, response -> assertThat(response).isEqualTo(SOME_RESPONSE));
    }

    @Test
    public void executesRequestWithContext() throws Exception {
        executeWithContext(SOME_REQUEST, response -> assertThat(response).isEqualTo(SOME_RESPONSE));
    }

    @Test
    public void reportsMetricsWithContext() throws Exception {
        executeWithContext(SOME_REQUEST, response -> {
            final ReportRawData report = reportFromContext().get(0);
            assertThat(report.getHttpRequest()).isEqualTo(SOME_REQUEST);
            assertThat(report.getHttpResponse()).isEqualTo(SOME_RESPONSE);
            assertThat(report.getStartTimestamp()).isLessThanOrEqualTo(report.getStopTimestamp());
        });
    }

    private void executeWithContext(final HttpRequest request, Consumer<HttpResponse> test) {
        Http.Context.current.set(emptyContext());
        executeWithoutContext(request, test);
    }

    private void executeWithoutContext(final HttpRequest request, Consumer<HttpResponse> test) {
        final MetricHttpClient metricHttpClient = MetricHttpClient.of(httpClient());
        final HttpResponse response = metricHttpClient.execute(request).toCompletableFuture().join();
        test.accept(response);
    }

    @SuppressWarnings("unchecked")
    private List<ReportRawData> reportFromContext() {
        return (List<ReportRawData>) Http.Context.current.get().args.get(KEY);
    }

    private Http.Context emptyContext() {
        final Http.Context context = new Http.Context(null, null, null, emptyMap(), emptyMap(), emptyMap());
        context.args.put(KEY, new LinkedList<>());
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
