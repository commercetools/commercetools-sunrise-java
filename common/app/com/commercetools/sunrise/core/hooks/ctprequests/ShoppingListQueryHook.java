package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface ShoppingListQueryHook extends FilterHook {

    CompletionStage<PagedQueryResult<ShoppingList>> on(ShoppingListQuery request, Function<ShoppingListQuery, CompletionStage<PagedQueryResult<ShoppingList>>> nextComponent);

    static CompletionStage<PagedQueryResult<ShoppingList>> run(final HookRunner hookRunner, final ShoppingListQuery request, final Function<ShoppingListQuery, CompletionStage<PagedQueryResult<ShoppingList>>> execution) {
        return hookRunner.run(ShoppingListQueryHook.class, request, execution, h -> h::on);
    }
}