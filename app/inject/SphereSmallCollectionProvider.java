package inject;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.queries.MetaModelQueryDsl;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public abstract class SphereSmallCollectionProvider<R, E> extends Base implements Provider<R> {
    private final SphereClient client;

    @Inject
    protected SphereSmallCollectionProvider(final SphereClient client) {
        this.client = client;
    }

    protected abstract MetaModelQueryDsl<E, ?, ?, ?> query();
    protected abstract R transform(final List<E> list);

    @Override
    public R get() {
        final MetaModelQueryDsl<E, ?, ?, ?> of = query();
        final List<E> elements = client.execute(of.withLimit(500)).toCompletableFuture().join().getResults();
        return transform(elements);
    }
}
