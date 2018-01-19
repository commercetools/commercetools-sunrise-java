package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface ProductProjectionQueryHook extends FilterHook {

    CompletionStage<PagedQueryResult<ProductProjection>> on(ProductProjectionQuery request, Function<ProductProjectionQuery, CompletionStage<PagedQueryResult<ProductProjection>>> nextComponent);

    static CompletionStage<PagedQueryResult<ProductProjection>> run(final HookRunner hookRunner, final ProductProjectionQuery request, final Function<ProductProjectionQuery, CompletionStage<PagedQueryResult<ProductProjection>>> execution) {
        return hookRunner.run(ProductProjectionQueryHook.class, request, execution, h -> h::on);
    }
}
