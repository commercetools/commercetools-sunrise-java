package com.commercetools.sunrise.common.ctp;

import io.sphere.sdk.http.HttpMethod;
import io.sphere.sdk.utils.SphereInternalLogger;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import play.libs.concurrent.HttpExecution;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Predicate;

import static com.commercetools.sunrise.common.ctp.MetricHttpClient.KEY;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;

/**
 * Action that registers metrics for every request.
 */
public final class MetricsLogger extends Action.Simple {

    private static final SphereInternalLogger LOGGER = SphereInternalLogger.getLogger("metrics.simple");

    @Override
    public CompletionStage<Result> call(final Http.Context ctx) {
        final CompletionStage<Result> result = delegate.call(ctx);
        result.thenAcceptAsync(res -> MetricsLogger.logRequestData(ctx), HttpExecution.defaultContext());
        return result;
    }

    private static void logRequestData(final Http.Context ctx) {
        final List<ReportRawData> rawData = findRawData(ctx);
        if (!rawData.isEmpty()) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(() -> format("commercetools requests in %s: \n%s", ctx.request(), formatQuery(rawData)));
            } else {
                final Pair<List<ReportRawData>, List<ReportRawData>> queryCommandPair = splitByQueriesAndCommands(rawData);
                final List<ReportRawData> queries = queryCommandPair.getLeft();
                final List<ReportRawData> commands = queryCommandPair.getRight();
                LOGGER.debug(() -> format("%s used %d requests (%d queries, %d commands, %dbytes fetched, in (%s)).",
                        ctx.request(),
                        rawData.size(),
                        queries.size(),
                        commands.size(),
                        calculateTotalSize(rawData),
                        calculateDuration(rawData)));
            }
        }
    }

    private static String formatQuery(final List<ReportRawData> rawData) {
        return rawData.stream().map(report -> {
            final String url = report.getHttpRequest().getUrl();
            final HttpMethod httpMethod = report.getHttpRequest().getHttpMethod();
            final long duration = report.getStopTimestamp() - report.getStartTimestamp();
            final Integer bodySize = getBodySize(report);
            return format("  %s %s %dms %dbytes", httpMethod, extractUrl(url), duration, bodySize);
        }).collect(joining("\n"));
    }

    private static String extractUrl(final String url) {
        final String[] split = StringUtils.split(url, "/", 4);
        return split.length == 4 ? "/" + split[3] : url;
    }

    private static String calculateDuration(final List<ReportRawData> rawData) {
        return rawData.stream()
                .map(data -> data.getStopTimestamp() - data.getStartTimestamp())
                .map(l -> Long.toString(l) + " ms")
                .collect(joining(", "));
    }

    private static List<ReportRawData> findRawData(final Http.Context ctx) {
        final Object obj = ctx.args.get(KEY);
        if (obj != null && obj instanceof List) {
            return findRawData(ctx);
        }
        return emptyList();
    }

    private static Pair<List<ReportRawData>, List<ReportRawData>> splitByQueriesAndCommands(final List<ReportRawData> rawData) {
        return partition(rawData, data -> data.getHttpRequest().getHttpMethod() == HttpMethod.GET || data.getHttpRequest().getUrl().contains("/product-projections/search"));
    }

    private static Integer calculateTotalSize(final List<ReportRawData> rawData) {
        return rawData.stream().map(MetricsLogger::getBodySize).reduce(0, (a, b) -> a + b);
    }

    private static Integer getBodySize(final ReportRawData elem) {
        return Optional.ofNullable(elem.getHttpResponse().getResponseBody())
                .map(b -> b.length).orElse(0);
    }

    /**
     * Partitions <code>list</code> in two lists according to <code>predicate</code>.
     * @param list the list which should be divided
     * @param predicate returns true if the element of <code>list</code> should belong to the first result list
     * @param <T> generic type of the list
     * @return the first list satisfies <code>predicate</code>, the second one not.
     */
    private static <T> Pair<List<T>, List<T>> partition(final List<T> list, final Predicate<T> predicate) {
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