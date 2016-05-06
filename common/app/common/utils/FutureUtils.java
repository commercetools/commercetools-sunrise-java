package common.utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public final class FutureUtils {

    private FutureUtils() {
    }

    public static <T> CompletionStage<List<T>> listOfFuturesToFutureOfList(final List<CompletionStage<T>> stageList) {
        final List<CompletableFuture<T>> futureList = stageList.stream()
                .map(CompletionStage::toCompletableFuture)
                .collect(toList());
        final CompletableFuture[] futuresAsArray = futureList.toArray(new CompletableFuture[futureList.size()]);
        return CompletableFuture.allOf(futuresAsArray)
                .thenApply(x -> futureList.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.<T>toList()));
    }
}
