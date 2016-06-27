package io.sphere.sdk.utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public final class FutureUtils {

    private FutureUtils() {
    }

    /**
     * Transforms a list of {@code CompletionStage} into a {@code CompletionStage} of a list,
     * that will be completed once all the elements of the given list are completed.
     * @param stageList list of {@code CompletionStage}
     * @param <T> the element obtained from the list of {@code CompletionStage}
     * @return the {@code CompletionStage} of a list of elements
     */
    public static <T> CompletionStage<List<T>> listOfFuturesToFutureOfList(final List<? extends CompletionStage<T>> stageList) {
        final List<CompletableFuture<T>> futureList = stageList.stream()
                .map(CompletionStage::toCompletableFuture)
                .collect(toList());
        final CompletableFuture[] futuresAsArray = futureList.toArray(new CompletableFuture[futureList.size()]);
        return CompletableFuture.allOf(futuresAsArray)
                .thenApply(x -> futureList.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.<T>toList()));
    }

    public static <T> CompletionStage<T> exceptionallyCompletedFuture(final Throwable throwable) {
        final CompletableFuture<T> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(throwable);
        return completableFuture;
    }

    public static <T> CompletionStage<T> recoverWithAsync(final CompletionStage<T> future, final Executor executor, final Function<? super Throwable, CompletionStage<T>> f) {
        final CompletableFuture<T> result = new CompletableFuture<>();
        final BiConsumer<T, Throwable> action = (value, error) -> {
            if (error == null) {
                result.complete(value);
            } else {
                final CompletionStage<T> alternative = f.apply(error);
                alternative.whenComplete((alternativeValue, alternativeError) -> {
                    if (alternativeValue != null) {
                        result.complete(alternativeValue);
                    } else {
                        result.completeExceptionally(alternativeError);
                    }
                });
            }
        };
        future.whenCompleteAsync(action, executor);
        return result;
    }
}
