package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.ShoppingListLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.ShoppingListQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * This base class provides an execution strategy to execute a {@link ShoppingListQuery} for a single {@link ShoppingList}
 * with the registred hooks {@link ShoppingListQueryHook} and {@link ShoppingListLoadedHook}.
 */
public abstract class AbstractSingleShoppingListQueryExecutor extends AbstractSphereRequestExecutor {

    @Inject
    protected AbstractSingleShoppingListQueryExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Optional<ShoppingList>> executeRequest(final ShoppingListQuery baseQuery) {
        final ShoppingListQuery request = ShoppingListQueryHook.runHook(getHookRunner(), baseQuery);
        return getSphereClient().execute(request)
                .thenApply(PagedQueryResult::head)
                .thenApplyAsync(shoppingListOpt -> {
                    shoppingListOpt.ifPresent(shoppingList -> ShoppingListLoadedHook.runHook(getHookRunner(), shoppingList));
                    return shoppingListOpt;
                }, HttpExecution.defaultContext());
    }
}
