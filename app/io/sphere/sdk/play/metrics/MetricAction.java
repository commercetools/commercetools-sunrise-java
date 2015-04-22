package io.sphere.sdk.play.metrics;

import io.sphere.sdk.http.HttpMethod;
import io.sphere.sdk.utils.ListUtils;
import io.sphere.sdk.utils.SphereInternalLogger;
import org.apache.commons.lang3.tuple.Pair;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
        return ListUtils.partition(rawDatas, data -> data.getHttpRequest().getHttpMethod() == HttpMethod.GET);
    }

    private Integer calculateTotalSize(final List<ReportRawData> rawDatas) {
        return rawDatas.stream().map(elem -> elem.getHttpResponse().getResponseBody().map(b -> b.length).orElse(0)).reduce(0, (a, b) -> a + b);
    }
}