package com.commercetools.sunrise.common.version;

import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.http.HttpHeaders;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.HttpResponse;
import io.sphere.sdk.projects.queries.ProjectGet;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.it.SphereClientFixtures.provideHttpClient;
import static com.commercetools.sunrise.it.SphereClientFixtures.provideSphereClient;
import static org.assertj.core.api.Assertions.assertThat;

public class SunriseSolutionInfoIntegrationTest {

    @Test
    public void userAgent() throws Exception {
        try (final FakeHttpClient httpClient = new FakeHttpClient()) {
            try (final BlockingSphereClient client = provideSphereClient(httpClient)) {
                client.executeBlocking(ProjectGet.of());
                assertThat(httpClient.getLastUserAgent())
                        .contains("sunrise-java-shop-framework");
            }
        }
    }

    private static class FakeHttpClient implements HttpClient {
        private final HttpClient delegate = provideHttpClient();
        private String lastUserAgent;

        @Override
        public CompletionStage<HttpResponse> execute(final HttpRequest httpRequest) {
            lastUserAgent = httpRequest.getHeaders().getHeader(HttpHeaders.USER_AGENT).stream().findFirst().orElse(null);
            return delegate.execute(httpRequest);
        }

        @Override
        public void close() {
            delegate.close();
        }

        @Nullable
        @Override
        public String getUserAgent() {
            return delegate.getUserAgent();
        }

        public String getLastUserAgent() {
            return lastUserAgent;
        }
    }
}