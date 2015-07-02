package io.sphere.sdk.queries;

import io.sphere.sdk.client.SphereClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;
import static java.util.stream.LongStream.rangeClosed;

public class QueryAll<T, C extends QueryDsl<T, C>> {
    private static final int DEFAULT_PAGE_SIZE = 500;
    private final QueryDsl<T, C> baseQuery;
    private final long pageSize;

    private QueryAll(final QueryDsl<T, C> baseQuery, final long pageSize) {
        this.baseQuery = baseQuery;
        this.pageSize = pageSize;
    }

    public CompletionStage<List<T>> run(final SphereClient client) {
        return queryPage(client, 0).thenCompose(result -> {
            final List<CompletableFuture<List<T>>> futureResults = new ArrayList<>();
            futureResults.add(completedFuture(result.getResults()));
            futureResults.addAll(queryNextPages(client, result.getTotal()));
            return transformListOfFuturesToFutureOfLists(futureResults);
        });
    }

    private List<CompletableFuture<List<T>>> queryNextPages(final SphereClient client, final long totalElements) {
        final long totalPages = totalElements / pageSize;
        return rangeClosed(1, totalPages)
                .mapToObj(page -> queryPage(client, page)
                        .thenApply(PagedQueryResult::getResults)
                        .toCompletableFuture())
                .collect(toList());
    }

    private CompletionStage<PagedQueryResult<T>> queryPage(final SphereClient client, final long pageNumber) {
        final QueryDsl<T, C> query = baseQuery
                .withOffset(pageNumber * pageSize)
                .withLimit(pageSize);
        return client.execute(query);
    }

    private CompletableFuture<List<T>> transformListOfFuturesToFutureOfLists(final List<CompletableFuture<List<T>>> futures) {
        final CompletableFuture[] futuresAsArray = futures.toArray(new CompletableFuture[futures.size()]);
        return CompletableFuture.allOf(futuresAsArray)
                .thenApply(x -> futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(Collection::stream)
                        .collect(Collectors.<T>toList()));
    }

    public static <T, C extends QueryDsl<T, C>> QueryAll<T, C> of(final QueryDsl<T, C> baseQuery) {
        return of(baseQuery, DEFAULT_PAGE_SIZE);
    }

    public static <T, C extends QueryDsl<T, C>> QueryAll<T, C> of(final QueryDsl<T, C> baseQuery, final int pageSize) {
        return new QueryAll<>(baseQuery, pageSize);
    }
}
