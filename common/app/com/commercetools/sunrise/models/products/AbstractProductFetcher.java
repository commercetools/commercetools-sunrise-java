package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.AbstractHookRunner;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractProductFetcher extends AbstractHookRunner<Optional<ProductWithVariant>, ProductProjectionQuery> implements ProductFetcher {

    private final SphereClient sphereClient;

    protected AbstractProductFetcher(final HookRunner hookRunner, final SphereClient sphereClient) {
        super(hookRunner);
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<Optional<ProductWithVariant>> get(final String productIdentifier, @Nullable final String variantIdentifier) {
        return buildRequest(productIdentifier, variantIdentifier)
                .map(request -> runHook(request, r -> sphereClient.execute(r)
                        .thenApply(results -> selectResult(results, productIdentifier, variantIdentifier))))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected CompletionStage<Optional<ProductWithVariant>> runHook(final ProductProjectionQuery request,
                                                                    final Function<ProductProjectionQuery, CompletionStage<Optional<ProductWithVariant>>> execution) {
        return hookRunner().run(ProductFetcherHook.class, request, execution, h -> h::on);
    }

    protected abstract Optional<ProductProjectionQuery> buildRequest(final String productIdentifier, final String variantIdentifier);

    protected Optional<ProductWithVariant> selectResult(final PagedQueryResult<ProductProjection> results,
                                                        final String productIdentifier, final String variantIdentifier) {
        return results.head().map(product -> ProductWithVariant.of(product, product.getMasterVariant()));
    }
}
