package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.ProductProjectionLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.ProductProjectionQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.concurrent.HttpExecution;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class AbstractSingleProductQueryExecutor extends AbstractSphereRequestExecutor {

    protected AbstractSingleProductQueryExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Optional<ProductProjection>> executeRequest(final ProductProjectionQuery baseRequest) {
        return executeRequest(baseRequest, PagedQueryResult::head);
    }

    protected final CompletionStage<Optional<ProductProjection>> executeRequest(final ProductProjectionQuery baseRequest,
                                                                                final Function<PagedQueryResult<ProductProjection>, Optional<ProductProjection>> productSelector) {
        final ProductProjectionQuery request = ProductProjectionQueryHook.runHook(getHookRunner(), baseRequest);
        final CompletionStage<PagedQueryResult<ProductProjection>> result = getSphereClient().execute(request);
        return result
                .thenApply(productSelector)
                .thenApplyAsync(productOpt -> {
                    productOpt.ifPresent(product -> ProductProjectionLoadedHook.runHook(getHookRunner(), product));
                    return productOpt;
                }, HttpExecution.defaultContext());
    }
}
