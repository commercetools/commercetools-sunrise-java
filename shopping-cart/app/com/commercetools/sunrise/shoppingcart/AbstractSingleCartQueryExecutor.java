package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.CartLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.CartQueryHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public abstract class AbstractSingleCartQueryExecutor extends AbstractSphereRequestExecutor {

    @Inject
    protected AbstractSingleCartQueryExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Optional<Cart>> executeRequest(final CartQuery baseQuery) {
        final CartQuery request = CartQueryHook.runHook(getHookRunner(), baseQuery);
        return getSphereClient().execute(request)
                .thenApply(PagedQueryResult::head)
                .thenApplyAsync(cartOpt -> {
                    cartOpt.ifPresent(cart -> CartLoadedHook.runHook(getHookRunner(), cart));
                    return cartOpt;
                }, HttpExecution.defaultContext());
    }
}
