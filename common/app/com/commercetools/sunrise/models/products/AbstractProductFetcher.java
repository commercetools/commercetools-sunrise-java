package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.AbstractSingleResourceFetcher;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.ProductProjectionLoadedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ProductWithVariantLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.ProductProjectionQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.concurrent.HttpExecution;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractProductFetcher extends AbstractSingleResourceFetcher<ProductProjection, ProductProjectionQuery, PagedQueryResult<ProductProjection>> implements ProductFetcher {

    protected AbstractProductFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Optional<ProductWithVariant>> get(final String productIdentifier, @Nullable final String variantIdentifier) {
        return defaultRequest(productIdentifier)
                .map(request -> executeRequest(request, pagedResult -> selectResource(pagedResult, productIdentifier, variantIdentifier))
                        .thenApplyAsync(productOpt -> productOpt.map(product -> {
                            final ProductWithVariant productWithVariant = ProductWithVariant.of(product, selectVariant(product, variantIdentifier));
                            runProductVariantLoadedHook(getHookRunner(), productWithVariant);
                            return productWithVariant;
                        }),
                        HttpExecution.defaultContext()))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected final CompletionStage<ProductProjectionQuery> runRequestHook(final HookRunner hookRunner, final ProductProjectionQuery baseRequest) {
        return ProductProjectionQueryHook.runHook(hookRunner, baseRequest);
    }

    @Override
    protected final void runResourceLoadedHook(final HookRunner hookRunner, final ProductProjection resource) {
        ProductProjectionLoadedHook.runHook(hookRunner, resource);
    }

    protected final void runProductVariantLoadedHook(final HookRunner hookRunner, final ProductWithVariant productWithVariant) {
        ProductWithVariantLoadedHook.runHook(hookRunner, productWithVariant);
    }

    @Override
    protected final Optional<ProductProjection> selectResource(final PagedQueryResult<ProductProjection> pagedResult) {
        return super.selectResource(pagedResult);
    }

    protected Optional<ProductProjection> selectResource(final PagedQueryResult<ProductProjection> pagedResult,
                                                         final String productIdentifier, @Nullable final String variantIdentifier) {
        return selectResource(pagedResult);
    }

    protected ProductVariant selectVariant(final ProductProjection product, @Nullable final String variantIdentifier) {
        return product.findFirstMatchingVariant().orElseGet(product::getMasterVariant);
    }
}
