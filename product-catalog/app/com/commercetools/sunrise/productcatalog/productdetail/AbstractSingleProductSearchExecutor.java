package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.ProductProjectionLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.ProductProjectionSearchHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import play.libs.concurrent.HttpExecution;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public abstract class AbstractSingleProductSearchExecutor extends AbstractSphereRequestExecutor {

    protected AbstractSingleProductSearchExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Optional<ProductProjection>> executeRequest(final ProductProjectionSearch baseRequest) {
        final ProductProjectionSearch request = ProductProjectionSearchHook.runHook(getHookRunner(), baseRequest);
        return getSphereClient().execute(request)
                .thenApply(PagedSearchResult::head)
                .thenApplyAsync(productOpt -> {
                    productOpt.ifPresent(product -> ProductProjectionLoadedHook.runHook(getHookRunner(), product));
                    return productOpt;
                }, HttpExecution.defaultContext());
    }
}
