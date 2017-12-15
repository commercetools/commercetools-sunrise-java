package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

public interface ShoppingListQueryHook extends CtpRequestHook {

    ShoppingListQuery onShoppingListQuery(final ShoppingListQuery cartQuery);

    static ShoppingListQuery runHook(final HookRunner hookRunner, final ShoppingListQuery shoppingListQuery) {
        return hookRunner.runUnaryOperatorHook(ShoppingListQueryHook.class, ShoppingListQueryHook::onShoppingListQuery, shoppingListQuery);
    }
}