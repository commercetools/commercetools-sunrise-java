package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.queries.CartQuery;

import java.util.concurrent.CompletionStage;

public interface CartQueryHook extends CtpRequestHook {

    CompletionStage<CartQuery> onCartQuery(final CartQuery query);

    static CompletionStage<CartQuery> runHook(final HookRunner hookRunner, final CartQuery query) {
        return hookRunner.runActionHook(CartQueryHook.class, CartQueryHook::onCartQuery, query);
    }
}