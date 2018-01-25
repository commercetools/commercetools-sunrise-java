package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.AbstractHookRunner;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class AbstractProductListFetcher extends AbstractHookRunner<PagedSearchResult<ProductProjection>, ProductProjectionSearch> implements ProductListFetcher {

    private final SphereClient sphereClient;

    protected AbstractProductListFetcher(final HookRunner hookRunner, final SphereClient sphereClient) {
        super(hookRunner);
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<PagedSearchResult<ProductProjection>> get() {
        return runHook(buildRequest(), sphereClient::execute);
    }

    @Override
    protected CompletionStage<PagedSearchResult<ProductProjection>> runHook(final ProductProjectionSearch request,
                                                                            final Function<ProductProjectionSearch, CompletionStage<PagedSearchResult<ProductProjection>>> execution) {
        return hookRunner().run(ProductListFetcherHook.class, request, execution, h -> h::on);
    }

    protected abstract ProductProjectionSearch buildRequest();
}
