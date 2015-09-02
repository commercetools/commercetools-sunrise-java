package io.sphere.sdk.play.metrics;

import io.sphere.sdk.http.HttpMethod;
import io.sphere.sdk.utils.SphereInternalLogger;
import org.apache.commons.lang3.tuple.Pair;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

import java.util.*;
import java.util.function.Predicate;

import static java.lang.String.*;
import static java.util.stream.Collectors.joining;

public class MetricAction extends play.mvc.Action.Simple {

    public static final String KEY = "io.sphere.sdk.play.metrics.reportRawData";
    private static final SphereInternalLogger LOGGER = SphereInternalLogger.getLogger("metrics.simple");

    public F.Promise<Result> call(final Http.Context ctx) throws Throwable {
        final List<ReportRawData> rawDatas = Collections.synchronizedList(new LinkedList<>());
        ctx.args.put(KEY, rawDatas);
        final F.Promise<Result> resultPromise = delegate.call(ctx);
        logRequestDataOnComplete(ctx, rawDatas, resultPromise);
        return resultPromise;
    }

    private void logRequestDataOnComplete(final Http.Context ctx, final List<ReportRawData> rawDatas, final F.Promise<Result> resultPromise) {
        resultPromise.onRedeem(r -> {
            final Pair<List<ReportRawData>, List<ReportRawData>> queryCommandPair = splitByQueriesAndCommands(rawDatas);
            final List<ReportRawData> queries = queryCommandPair.getLeft();
            final List<ReportRawData> commands = queryCommandPair.getRight();
            final int size = calculateTotalSize(rawDatas);
            final String durations = rawDatas.stream().map(data -> data.getStopTimestamp() - data.getStartTimestamp()).map(l -> Long.toString(l) + " ms").collect(joining(", "));
            LOGGER.debug(() -> format("%s used %d requests (%d queries, %d commands, %d bytes fetched, in (%s)).", ctx.request(), rawDatas.size(), queries.size(), commands.size(), size, durations));
        });
    }

    private Pair<List<ReportRawData>, List<ReportRawData>> splitByQueriesAndCommands(final List<ReportRawData> rawDatas) {
        return partition(rawDatas, data -> data.getHttpRequest().getHttpMethod() == HttpMethod.GET);
    }

    private Integer calculateTotalSize(final List<ReportRawData> rawDatas) {
        return rawDatas.stream().map(elem -> Optional.ofNullable(elem.getHttpResponse().getResponseBody())
                .map(b -> b.length).orElse(0)).reduce(0, (a, b) -> a + b);
    }

    /**
     * Partitions <code>list</code> in two lists according to <code>predicate</code>.
     * @param list the list which should be divided
     * @param predicate returns true if the element of <code>list</code> should belong to the first result list
     * @param <T> generic type of the list
     * @return the first list satisfies <code>predicate</code>, the second one not.
     */
    public static <T> Pair<List<T>, List<T>> partition(final List<T> list, final Predicate<T> predicate) {
        final List<T> matchingPredicate = new ArrayList<>();
        final List<T> notMatchingPredicate = new ArrayList<>();
        for (final T element : list) {
            if (predicate.test(element)) {
                matchingPredicate.add(element);
            } else {
                notMatchingPredicate.add(element);
            }
        }
        return Pair.of(matchingPredicate, notMatchingPredicate);
    }
}