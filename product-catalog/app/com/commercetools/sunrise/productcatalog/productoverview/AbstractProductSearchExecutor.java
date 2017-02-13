package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.hooks.events.ProductProjectionPagedSearchResultLoadedHook;
import com.commercetools.sunrise.hooks.requests.ProductProjectionSearchHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractProductSearchExecutor extends AbstractSphereRequestExecutor {

    protected AbstractProductSearchExecutor(final SphereClient sphereClient, final HookContext hookContext) {
        super(sphereClient, hookContext);
    }

    protected final CompletionStage<PagedSearchResult<ProductProjection>> executeRequest(final ProductProjectionSearch baseRequest) {
        final ProductProjectionSearch request = ProductProjectionSearchHook.runHook(getHookContext(), baseRequest);
        return getSphereClient().execute(request)
                .thenApplyAsync(result -> {
                    ProductProjectionPagedSearchResultLoadedHook.runHook(getHookContext(), result);
                    return result;
                }, HttpExecution.defaultContext());
    }
}
