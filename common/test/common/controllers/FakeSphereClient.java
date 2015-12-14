package common.controllers;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class FakeSphereClient implements SphereClient {
    private CompletionStage<?> fakeResponse;

    private FakeSphereClient(final CompletionStage<?> fakeResponse) {
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
    public String toString() {
        return "FakeSphereClient";
    }

    public static <T> SphereClient ofResponse(final T result) {
        return new FakeSphereClient(CompletableFuture.completedFuture(result));
    }

    public static SphereClient ofUnhealthyCtp() {
        return new FakeSphereClient(CompletableFuture.completedFuture(null));
    }

    public static SphereClient ofUnresponsiveCtp() {
        return new FakeSphereClient(new CompletableFuture<>());
    }
}
