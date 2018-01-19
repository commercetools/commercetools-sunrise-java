package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface CartQueryHook extends FilterHook {

    CompletionStage<PagedQueryResult<Cart>> on(CartQuery request, Function<CartQuery, CompletionStage<PagedQueryResult<Cart>>> nextComponent);

    static CompletionStage<PagedQueryResult<Cart>> run(final HookRunner hookRunner, final CartQuery request, final Function<CartQuery, CompletionStage<PagedQueryResult<Cart>>> execution) {
        return hookRunner.run(CartQueryHook.class, request, execution, h -> h::on);
    }
}