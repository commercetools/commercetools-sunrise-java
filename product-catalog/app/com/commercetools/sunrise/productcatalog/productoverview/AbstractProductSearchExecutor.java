package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.ProductProjectionPagedSearchResultLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.ProductProjectionSearchHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractProductSearchExecutor extends AbstractSphereRequestExecutor {

    protected AbstractProductSearchExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<PagedSearchResult<ProductProjection>> executeRequest(final ProductProjectionSearch baseRequest) {
        final ProductProjectionSearch request = ProductProjectionSearchHook.runHook(getHookRunner(), baseRequest);
        return getSphereClient().execute(request)
                .thenApplyAsync(result -> {
                    ProductProjectionPagedSearchResultLoadedHook.runHook(getHookRunner(), result);
                    return result;
                }, HttpExecution.defaultContext());
    }
}
