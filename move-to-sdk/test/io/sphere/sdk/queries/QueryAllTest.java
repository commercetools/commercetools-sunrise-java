package io.sphere.sdk.queries;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

public class QueryAllTest {
    private static final int PAGE_SIZE = 5;

    @Test
    public void onEmptyResult() throws Exception {
        withClient(clientWithResults(0), list -> assertThat(list).isSorted().isEmpty());
    }

    @Test
    public void onOnePageResult() throws Exception {
        withClient(clientWithResults(3), list -> assertThat(list).isSorted().hasSize(3));
    }

    @Test
    public void onMultiplePagesResult() throws Exception {
        withClient(clientWithResults(16), list -> assertThat(list).isSorted().hasSize(16));
    }

    @Test
    public void onWrongTotalResult() throws Exception {
        withClient(clientWithWrongResults(16), list -> assertThat(list).isSorted().hasSize(16));
    }

    @Test
    public void onUnsortedResponses() throws Exception {
        withClient(clientWithDelays(16), list -> assertThat(list).isSorted().hasSize(16));
    }

    private void withClient(final SphereClient client, final Consumer<List> test) {
        final QueryAll<Category, CategoryQuery> query = QueryAll.of(CategoryQuery.of(), 5);
        final List elements = query.run(client).toCompletableFuture().join();
        test.accept(elements);
    }

    private SphereClient clientWithResults(final int totalResults) {
        return client(totalResults, 0, false);
    }

    private SphereClient clientWithWrongResults(final int totalResults) {
        return client(totalResults, 10, false);
    }

    private SphereClient clientWithDelays(final int totalResults) {
        return client(totalResults, 0, true);
    }

    private <C> SphereClient client(final int totalResults, final int deviation, final boolean withDelays) {
        return new SphereClient() {

            @SuppressWarnings("unchecked")
            @Override
            public <T> CompletionStage<T> execute(final SphereRequest<T> request) {
                final QueryDsl<T, C> query = (QueryDsl<T, C>) request;
                final int offset = query.offset().get().intValue();
                return CompletableFuture.supplyAsync(() -> {
                    if (withDelays) {
                        try {
                            Thread.sleep(100 / (offset + 1));
                        } catch (InterruptedException e) {
                            throw new CompletionException(e);
                        }
                    }
                    return (T) generatePagedQueryResult(offset);
                });
            }

            @Override
            public void close() {

            }

            @SuppressWarnings("unchecked")
            private <T> PagedQueryResult<T> generatePagedQueryResult(final int offset) {
                final int total = totalResults + deviation;
                final int count = min(PAGE_SIZE, max(totalResults - offset, 0));
                final List<T> results = (List<T>) generateSortedResultList(offset, count);
                return PagedQueryResult.of(offset, total, results);
            }

            private List<Integer> generateSortedResultList(final int offset, final int count) {
                return range(offset, offset + count)
                        .boxed()
                        .collect(toList());
            }
        };
    }
}
