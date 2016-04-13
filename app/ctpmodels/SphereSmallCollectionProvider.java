package ctpmodels;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.queries.QueryDsl;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;
import static io.sphere.sdk.queries.QueryExecutionUtils.queryAll;

public abstract class SphereSmallCollectionProvider<R, E, Q extends QueryDsl<E, Q>> extends Base implements Provider<R> {
    private final SphereClient client;

    @Inject
    protected SphereSmallCollectionProvider(final SphereClient client) {
        this.client = client;
    }

    protected abstract Q query();
    protected abstract R transform(final List<E> list);

    @Override
    public R get() {
        final List<E> elements = blockingWait(queryAll(client, query()), 30, TimeUnit.SECONDS);
        return transform(elements);
    }
}
