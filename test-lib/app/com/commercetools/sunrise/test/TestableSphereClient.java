package com.commercetools.sunrise.test;

import io.sphere.sdk.client.SphereApiConfig;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.client.TimeoutSphereClientDecorator;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class TestableSphereClient implements SphereClient {

    private CompletionStage<?> fakeResponse;

    private TestableSphereClient(final CompletionStage<?> fakeResponse) {
        this.fakeResponse = fakeResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> CompletionStage<T> execute(final SphereRequest<T> sphereRequest) {
        return (CompletionStage<T>) fakeResponse;
    }

    @Override
    public void close() {

    }

    @Override
    public SphereApiConfig getConfig() {
        return new SphereApiConfig() {
            @Override
            public String getApiUrl() {
                return "fakeUrl";
            }

            @Override
            public String getProjectKey() {
                return "fakeProject";
            }
        };
    }

    @Override
    public String toString() {
        return "TestableSphereClient";
    }

    public static <T> SphereClient ofResponse(final T result) {
        return new TestableSphereClient(CompletableFuture.completedFuture(result));
    }

    public static SphereClient ofEmptyResponse() {
        return new TestableSphereClient(CompletableFuture.completedFuture(""));
    }

    public static SphereClient ofUnhealthyPlatform() {
        final CompletableFuture<Object> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Unhealthy CTP client"));
        return new TestableSphereClient(future);
    }

    public static SphereClient ofUnresponsivePlatform() {
        final TestableSphereClient client = new TestableSphereClient(new CompletableFuture<>());
        return TimeoutSphereClientDecorator.of(client, 1L, TimeUnit.MILLISECONDS);
    }
}
