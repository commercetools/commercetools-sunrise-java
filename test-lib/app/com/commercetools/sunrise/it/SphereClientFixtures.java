package com.commercetools.sunrise.it;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.client.SphereAccessTokenSupplier;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.http.AsyncHttpClientAdapter;
import io.sphere.sdk.http.HttpClient;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import java.time.Duration;

public final class SphereClientFixtures {

    public static BlockingSphereClient provideSphereClient() {
        return provideSphereClient(provideHttpClient(), provideSphereClientConfig());
    }

    public static BlockingSphereClient provideSphereClient(final HttpClient httpClient) {
        return provideSphereClient(httpClient, provideSphereClientConfig());
    }

    public static BlockingSphereClient provideSphereClient(final HttpClient httpClient, final SphereClientConfig sphereClientConfig) {
        final SphereAccessTokenSupplier tokenSupplier = SphereAccessTokenSupplier.ofAutoRefresh(sphereClientConfig, httpClient, false);
        final SphereClient underlying = SphereClient.of(sphereClientConfig, httpClient, tokenSupplier);
        return BlockingSphereClient.of(underlying, Duration.ofSeconds(30));
    }

    public static HttpClient provideHttpClient() {
        final AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient(new DefaultAsyncHttpClientConfig.Builder().setAcceptAnyCertificate(true).build());
        return AsyncHttpClientAdapter.of(asyncHttpClient);
    }

    private static SphereClientConfig provideSphereClientConfig() {
        final Config configuration = ConfigFactory.load("it.conf");
        final String projectKey = configuration.getString("ctp.it.projectKey");
        final String clientId = configuration.getString("ctp.it.clientId");
        final String clientSecret = configuration.getString("ctp.it.clientSecret");
        return SphereClientConfig.of(projectKey, clientId, clientSecret);
    }
}
