package com.commercetools.sunrise.framework.hooks.ctprequests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.carts.queries.CartQuery;

public interface CartQueryHook extends CtpRequestHook {

    CartQuery onCartQuery(final CartQuery cartQuery);

    static CartQuery runHook(final HookRunner hookRunner, final CartQuery cartQuery) {
        return hookRunner.runUnaryOperatorHook(CartQueryHook.class, CartQueryHook::onCartQuery, cartQuery);
    }
}