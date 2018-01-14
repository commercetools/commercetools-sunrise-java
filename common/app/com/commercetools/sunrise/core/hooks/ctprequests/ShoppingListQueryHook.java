package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

import java.util.concurrent.CompletionStage;

public interface ShoppingListQueryHook extends CtpRequestHook {

    CompletionStage<ShoppingListQuery> onShoppingListQuery(final ShoppingListQuery query);

    static CompletionStage<ShoppingListQuery> runHook(final HookRunner hookRunner, final ShoppingListQuery query) {
        return hookRunner.runActionHook(ShoppingListQueryHook.class, ShoppingListQueryHook::onShoppingListQuery, query);
    }
}